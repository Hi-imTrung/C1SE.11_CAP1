package com.example.iHome.controller;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.EmailTemplateDTO;
import com.example.iHome.model.dto.MailDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.EmailService;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.ValidatorUtil;
import com.example.iHome.validator.ForgotPasswordValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private static final String REDIRECT_URL = "/forgot_password";

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ForgotPasswordValidator forgotPasswordValidator;

    @GetMapping(value = {"", "/"})
    public String view(Model model) {
        try {
            model.addAttribute("accountDTO", new AccountDTO());
            model.addAttribute("error", null);

            return "forgot_password";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("")
    public String forgotPassword(Model model, @Valid AccountDTO accountDTO) {
        try {
            MessageDTO messageDTO;
            String email = accountDTO.getEmail();
            Account account = accountService.findByEmail(email);

            if (account == null) {
                messageDTO = new MessageDTO("forgotPassword", "warning", "Tài khoản không tồn tại trong hệ thống!");

                model.addAttribute("messageDTO", messageDTO);
                model.addAttribute("accountDTO", accountDTO);
                return "forgot_password";
            } else {
                Thread thread = sendEmail(account);
                thread.start();
                messageDTO = new MessageDTO("forgotPassword", "success", "Gửi yêu cầu thành công vui lòng kiểm tra email!");
            }
            model.addAttribute("messageDTO", messageDTO);

            return "forgot_password_success";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/confirm")
    public String confirmForgotPassword(Model model, @RequestParam String ref) {
        try {
            AccountDTO accountDTO = new AccountDTO();
            if (ValidatorUtil.isEmpty(ref)) {
                model.addAttribute("messageDTO", new MessageDTO("confirmForgotPassword", "warning",
                        "Tài khoản không tồn tại trong hệ thống!"));
                model.addAttribute("accountDTO", accountDTO);
            } else {
                String result = decoderBase64ToString(ref);
                ObjectMapper objectMapper = new ObjectMapper();
                MailDTO mailDTO = objectMapper.readValue(result, MailDTO.class);
                Date currentTime = new Date();
                Date time = DateUtil.convertStringToDate(mailDTO.getTime(), "HH:mm:ss dd-MM-yyyy");

                // adding 30 minutes to the time
                Date afterAdding30Minutes = new Date(time.getTime() + (30 * 60 * 1000));
                if (currentTime.after(afterAdding30Minutes)) {
                    model.addAttribute("messageDTO", new MessageDTO("forgotPassword", "warning",
                            "Đã hết thời gian xác thực tài khoản vui lòng gửi lại yêu cầu!"));
                    model.addAttribute("accountDTO", accountDTO);
                    return "forgot_password_form";
                }

                LinkedHashMap<String, String> linkedHashMap = ((LinkedHashMap) mailDTO.getData());
                long id = Long.parseLong(String.valueOf(linkedHashMap.get("id")));
                accountDTO.setId(id);
                model.addAttribute("accountDTO", accountDTO);
                model.addAttribute("errorList", new HashMap<>());
            }

            return "forgot_password_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/confirm")
    public String confirmForgotPassword(Model model, @Valid AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            forgotPasswordValidator.validate(accountDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                model.addAttribute("messageDTO", new MessageDTO("forgotPassword", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("accountDTO", accountDTO);
                return "forgot_password_form";
            }
            Account account = accountService.findById(accountDTO.getId());
            account.setPassword(passwordEncoder.encode(accountDTO.getNewPassword()));
            accountService.save(account);


            model.addAttribute("messageDTO", new MessageDTO("forgotPassword", "success",
                    "Bạn đã thay đổi mật khẩu thành công!"));
            return "forgot_password_success";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL + "/confirm";
        }
    }

    private Thread sendEmail(Account account) {
        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setId(account.getId());
        emailTemplateDTO.setFullName(account.getFullName());
        emailTemplateDTO.setEmail(account.getEmail());

        return new Thread(() -> {
            try {
                emailService.sendForgotPassword(emailTemplateDTO);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private String decoderBase64ToString(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(text);
        return new String(decodedByteArray);
    }

}

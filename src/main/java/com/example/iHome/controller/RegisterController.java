package com.example.iHome.controller;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.EmailTemplateDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Role;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.model.mapper.RoleMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.EmailService;
import com.example.iHome.service.RoleService;
import com.example.iHome.validator.RegisterValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final String REDIRECT_URL = "/register";

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegisterValidator registerValidator;

    @GetMapping(value = {"", "/"})
    public String view(Model model) {
        try {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setEmail("");
            accountDTO.setPassword("");

            Role role = roleService.findByName("USER");
            if (role != null) {
                accountDTO.setRoleDTO(roleMapper.toDTO(role));
                accountDTO.setRoleId(role.getId());
            }

            model.addAttribute("accountDTO", accountDTO);
            model.addAttribute("error", null);

            return "register";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/success")
    public String registerSuccess(Model model, @RequestParam(required = false) String email) {
        try {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setEmail(email);

            model.addAttribute("messageDTO", null);
            model.addAttribute("accountDTO", accountDTO);

            return "register_success";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("")
    public String register(Model model, @Valid AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            registerValidator.validate(accountDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("accountDTO", accountDTO);
                return "register";
            }

            Account account = accountService.register(accountDTO);

            // Send Email For Register
            Thread thread = sendEmail(account);
            thread.start();

            String url = "/register/success?email=" + account.getEmail();
            return "redirect:" + url;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(Model model, @RequestParam(required = false) long id) {
        try {
            Account account = accountService.verifyAccount(id);
            if (account != null) {
                return "redirect:/login";
            } else {
                return "redirect:/error/404";
            }
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    private Thread sendEmail(Account account) {
        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setId(account.getId());
        emailTemplateDTO.setEmail(account.getEmail());

        return new Thread(() -> {
            try {
                emailService.sendForRegister(emailTemplateDTO);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

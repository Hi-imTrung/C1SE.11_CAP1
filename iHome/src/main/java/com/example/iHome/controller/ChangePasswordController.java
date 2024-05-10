package com.example.iHome.controller;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.validator.ChangePasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/change-password")
public class ChangePasswordController {

    private static final String REDIRECT_URL = "/change-password";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ChangePasswordValidator changePasswordValidator;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = {"", "/"})
    public String view(Model model, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();

        model.addAttribute("accountDTO", accountMapper.toDTO(account));
        return "change_password";
    }

    @PostMapping(value = {"", "/"})
    public String save(Model model, Authentication authentication, @Valid AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account account = customUserDetails.getAccount();

            changePasswordValidator.validate(accountDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("accountDTO", accountDTO);
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                return "change_password";
            }
            // save
            String encodedPassword = passwordEncoder.encode(accountDTO.getNewPassword());
            account.setPassword(encodedPassword);
            accountService.save(account);

            return "redirect:/logout";
        } catch (Exception ex) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}

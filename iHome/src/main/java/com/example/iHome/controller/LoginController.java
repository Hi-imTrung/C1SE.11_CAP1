package com.example.iHome.controller;

import com.example.iHome.model.dto.AccountDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final String REDIRECT_URL = "/login";

    @GetMapping(value = {"", "/"})
    public String login(Model model) {
        try {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setEmail("");
            accountDTO.setPassword("");

            model.addAttribute("accountDTO", accountDTO);
            model.addAttribute("error", null);
            return "login";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}

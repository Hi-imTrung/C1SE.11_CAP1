package com.example.iHome.controller;

import com.example.iHome.model.dto.AccountDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/login")
    public String showErrorLogin(Model model, @RequestParam(required = false) String username) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(username);
        accountDTO.setPassword("");

        model.addAttribute("accountDTO", accountDTO);
        model.addAttribute("error", "error");

        return "login";
    }

    @GetMapping("/404")
    public String showError404() {
        return "error_404";
    }

}

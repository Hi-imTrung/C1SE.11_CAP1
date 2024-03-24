package com.example.iHome.controller.front;

import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("")
    public String view(Model model, Authentication authentication, @RequestParam long id) {
        return "";
    }

}

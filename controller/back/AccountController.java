package com.example.iHome.controller.back;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.validator.AccountValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/account")
public class AccountController {

    private static final String REDIRECT_URL = "/back/account";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountValidator accountValidator;

    @GetMapping(value = {"", "/"})
    public String list(Model model) {
        try {
            List<Account> accountList = accountService.findAll();
            model.addAttribute("accountDTOList", accountMapper.toListDTO(accountList));
            return "back/account_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping(value = {"/form", "/form/"})
    public String create(Model model) {
        try {
            AccountDTO accountDTO = new AccountDTO();

            model.addAttribute("accountDTO", accountDTO);
            return "back/account_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String edit(Model model, @PathVariable long id,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Account account = accountService.findById(id);

            model.addAttribute("accountDTO", accountMapper.toDTO(account));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/account_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, @Valid AccountDTO accountDTO, BindingResult bindingResult) {
        try {
            accountValidator.validate(accountDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("accountDTO", accountDTO);
                return "back/account_form";
            }
            Account account = accountService.save(accountDTO);

            String redirectUrl = "/back/account/form/" + account.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}

package com.example.iHome.controller.back;

import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/back/customer")
public class CustomerController {

    private static final String REDIRECT_URL = "/back/customer";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping(value = {"", "/"})
    public String list(Model model) {
        try {
            List<Customer> customerList = customerService.findAll();

            model.addAttribute("customerDTOList", customerMapper.toListDTO(customerList));
            return "back/customer_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String detail(Model model, @PathVariable long id,
                         @RequestParam(required = false) String action,
                         @RequestParam(required = false) String status) {
        try {
            Customer customer = customerService.findById(id);

            model.addAttribute("customerDTO", customerMapper.toDTO(customer));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/customer_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}

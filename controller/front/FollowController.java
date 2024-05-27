package com.example.iHome.controller.front;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.Follow;
import com.example.iHome.model.mapper.FollowMapper;
import com.example.iHome.service.CustomerService;
import com.example.iHome.service.FollowService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowMapper followMapper;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account owner = customUserDetails.getAccount();
        Customer customer = customerService.findByAccount(owner);

        List<Follow> follows = followService.findByCustomer(customer);

        if (action == null) {
            model.addAttribute("messageDTO", null);
        } else {
            model.addAttribute("messageDTO", new MessageDTO(action, status,
                    status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
        }

        model.addAttribute("followDTOList", followMapper.toListDTO(follows));
        return "front/follow_list";
    }

    @GetMapping("/add")
    public String add(Model model, Authentication authentication, @RequestParam long houseId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account owner = customUserDetails.getAccount();
        Customer customer = customerService.findByAccount(owner);

        Follow follow = new Follow();
        follow.setCustomer(customer);
        follow.setHouse(houseService.findById(houseId));
        follow.setStatus(true);
        followService.save(follow);

        String redirectUrl = "/follow" + "?action=save&status=success";
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/remove")
    public String remove(Model model, @RequestParam long followId) {
        Follow follow = followService.findById(followId);
        follow.setStatus(false);
        followService.save(follow);

        String redirectUrl = "/follow" + "?action=save&status=success";
        return "redirect:" + redirectUrl;
    }

}

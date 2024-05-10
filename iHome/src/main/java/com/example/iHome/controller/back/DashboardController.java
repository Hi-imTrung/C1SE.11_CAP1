package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.BookingService;
import com.example.iHome.service.DeviceService;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/back/dashboard")
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;


    @GetMapping(value = {"", "/"})
    public String dashboard(Model model, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account owner = customUserDetails.getAccount();

        List<House> houseList;
        List<Device> deviceList;
        List<Booking> bookingList;

        List<Booking> bookingListByProgress;

        // if account login has admin role -> show all
        if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {

            List<Account> accountList = accountService.findAll();
            model.addAttribute("totalAccount", accountList.size());

            houseList = houseService.findAll();
            deviceList = deviceService.findAll();
            bookingList = bookingService.findAll();
            bookingListByProgress = bookingService.findByProgress(ConstantUtil.PROGRESS_PENDING);
        } else {
            houseList = houseService.findByOwner(owner);
            deviceList = deviceService.findByOwner(owner.getId());
            bookingList = bookingService.findByOwner(owner.getId());
            bookingListByProgress = bookingService.findByProgressAndOwner(ConstantUtil.PROGRESS_PENDING, owner.getId());
        }

        model.addAttribute("totalHouse", houseList.size());
        model.addAttribute("totalDevice", deviceList.size());
        model.addAttribute("totalBooking", bookingList.size());
        model.addAttribute("bookingDTOListByProgress", bookingMapper.toListDTO(bookingListByProgress));

        return "back/dashboard";
    }

}

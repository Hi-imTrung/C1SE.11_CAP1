package com.example.iHome.controller.front;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.*;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.*;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import com.example.iHome.util.ValidatorUtil;
import com.example.iHome.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private BookingValidator bookingValidator;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/{houseId}")
    public String view(Model model, Authentication authentication, @PathVariable long houseId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();

        Customer customer = customerService.findByAccount(account);
        House house = houseService.findById(houseId);

        Date date = new Date();

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerDTO(customerMapper.toDTO(customer));
        bookingDTO.setPhone(customer.getAccount().getPhone());
        bookingDTO.setFullName(customer.getAccount().getFullName());
        bookingDTO.setCustomerId(customer.getId());
        bookingDTO.setIdentityImage(customer.getIdentityImage());
        bookingDTO.setHouseDTO(houseMapper.toDTO(house));
        bookingDTO.setHouseId(house.getId());
        bookingDTO.setPrice(FormatUtils.formatNumber(house.getPrice()));
        bookingDTO.setDate(DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN));

        model.addAttribute("bookingDTO", bookingDTO);
        return "front/checkout";
    }

    @PostMapping("")
    public String createBooking(Model model, BookingDTO bookingDTO, BindingResult bindingResult) {

        bookingValidator.validate(bookingDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            House house = houseService.findById(bookingDTO.getHouseId());
            Customer customer = customerService.findById(bookingDTO.getCustomerId());
            bookingDTO.setHouseDTO(houseMapper.toDTO(house));
            bookingDTO.setCustomerDTO(customerMapper.toDTO(customer));

            model.addAttribute("bookingDTO", bookingDTO);
            model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                    "Vui lòng kiểm tra lại thông tin!"));
            return "front/checkout";
        }

        if (ValidatorUtil.isEmpty(bookingDTO.getIdentityImage())) {
            House house = houseService.findById(bookingDTO.getHouseId());
            Customer customer = customerService.findById(bookingDTO.getCustomerId());
            bookingDTO.setHouseDTO(houseMapper.toDTO(house));
            bookingDTO.setCustomerDTO(customerMapper.toDTO(customer));

            model.addAttribute("bookingDTO", bookingDTO);
            model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                    "Vui lòng cập nhật CCCD / CMND!"));
            return "front/checkout";
        }

        bookingDTO.setProgress("PENDING");
        if (!ValidatorUtil.isFileEmpty(bookingDTO.getBillMul())) {
            FileDTO fileDTO = uploadFileService.uploadFile(bookingDTO.getBillMul(), ConstantUtil.TYPE_UPLOAD_IMAGE);
            bookingDTO.setBillImage(fileDTO.getPath());
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        bookingService.save(booking);

        Account account = booking.getCustomer().getAccount();
        // Send Email For Checkout
        Thread thread = sendEmail(account, booking, "BOOKING");
        thread.start();

        Thread threadSendMailForOwner = sendEmail(booking.getHouse().getOwner(), booking, "OWNER");
        threadSendMailForOwner.start();

        String redirectUrl = "/profile?action=save&status=success";
        return "redirect:" + redirectUrl;
    }

    private Thread sendEmail(Account account, Booking booking, String type) {
        EmailBookingDTO emailBookingDTO = new EmailBookingDTO();
        emailBookingDTO.setId(account.getId());
        emailBookingDTO.setEmail(account.getEmail());
        emailBookingDTO.setAddress(booking.getHouse().getAddress() + ", " + booking.getHouse().getDistricts() + ", " + booking.getHouse().getCity());
        emailBookingDTO.setName(booking.getHouse().getName());
        emailBookingDTO.setPrice(FormatUtils.formatNumber(booking.getPrice()) + " VNĐ");
        emailBookingDTO.setDate(DateUtil.convertDateToString(booking.getDate(), ConstantUtil.DATE_PATTERN));

        return new Thread(() -> {
            try {
                emailService.sendForCheckout(emailBookingDTO, type);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

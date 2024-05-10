package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.BookingDTO;
import com.example.iHome.model.dto.EmailBookingDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.service.BookingService;
import com.example.iHome.service.EmailService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/booking")
public class BookingController {

    private static final String REDIRECT_URL = "/back/booking";

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<Booking> bookingList;

            // If the account has the role is admin, get all the bookings.
            // Else get the bookings of the account.
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                bookingList = bookingService.findAll();
            } else {
                bookingList = bookingService.findByOwner(owner.getId());
            }

            model.addAttribute("bookingDTOList", bookingMapper.toListDTO(bookingList));
            return "back/booking_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String edit(Model model, @PathVariable long id, Authentication authentication,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            Booking booking = bookingService.findById(id);

            model.addAttribute("bookingDTO", bookingMapper.toDTO(booking));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/booking_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(BookingDTO bookingDTO) {
        try {

            Booking booking = bookingService.findById(bookingDTO.getId());

            if (bookingDTO.getProgress().equals(booking.getProgress())); {
                Account account = booking.getCustomer().getAccount();
                // Send Email For Checkout
                Thread thread = sendEmail(account, booking, "BOOKING");
                thread.start();

            }
            booking.setProgress(bookingDTO.getProgress());
            bookingService.save(booking);

            String redirectUrl = "/back/booking/form/" + booking.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    private Thread sendEmail(Account account, Booking booking, String type) {
        EmailBookingDTO emailBookingDTO = new EmailBookingDTO();
        emailBookingDTO.setId(account.getId());
        emailBookingDTO.setEmail(account.getEmail());
        emailBookingDTO.setAddress(booking.getHouse().getAddress() + ", " + booking.getHouse().getDistricts() + ", " + booking.getHouse().getCity().getName());
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

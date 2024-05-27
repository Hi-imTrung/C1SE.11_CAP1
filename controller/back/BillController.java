package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.BillDTO;
import com.example.iHome.model.dto.EmailBookingDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.BillMapper;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.BillService;
import com.example.iHome.service.BookingService;
import com.example.iHome.service.EmailService;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import com.example.iHome.validator.BillValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/bill")
public class BillController {

    private static final String REDIRECT_URL = "/back/bill";

    @Autowired
    private BillService billService;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BillValidator billValidator;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<Bill> billList;

            // If the account has the role is admin, get all the bills.
            // Else get the bills of the account.
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                billList = billService.findAll();
            } else {
                billList = billService.findByOwner(owner.getId());
            }

            model.addAttribute("billDTOList", billMapper.toListDTO(billList));
            return "back/bill_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form")
    public String create(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<House> houseList;
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                houseList = houseService.findAll();
            } else {
                houseList = houseService.findByOwner(owner);
            }
            BillDTO billDTO = new BillDTO();
            billDTO.setOwnerName(owner.getFullName());

            model.addAttribute("billDTO", billDTO);
            model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));

            return "back/bill_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String edit(Model model, @PathVariable long id, Authentication authentication,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<House> houseList;

            // If the account has the role is admin, get all the houses.
            // Else get the houses of the account.
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                houseList = houseService.findAll();
            } else {
                houseList = houseService.findByOwner(owner);
            }
            Bill bill = billService.findById(id);

            model.addAttribute("billDTO", billMapper.toDTO(bill));
            model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/bill_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, @Valid BillDTO billDTO, BindingResult bindingResult, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();

            billDTO.setPrice(FormatUtils.toEncodePrice(billDTO.getPrice()));
            billValidator.validate(billDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                List<House> houseList;
                if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                    houseList = houseService.findAll();
                } else {
                    houseList = houseService.findByOwner(owner);
                }
                billDTO.setOwnerName(owner.getFullName());
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("billDTO", billDTO);
                model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));
                return "back/bill_form";
            }

            Bill bill = billMapper.toEntity(billDTO);
            billService.save(bill);

            List<Booking> bookingList = bookingService.findByHouseAndProgress(bill.getHouse(), "APPROVED");
            for (Booking booking : bookingList) {
                Account account = booking.getCustomer().getAccount();
                // Send Email For Checkout
                Thread thread = sendEmail(account, bill, "BILL");
                thread.start();
            }

            String redirectUrl = "/back/bill/form/" + bill.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    private Thread sendEmail(Account account, Bill bill, String type) {
        EmailBookingDTO emailBookingDTO = new EmailBookingDTO();
        emailBookingDTO.setId(account.getId());
        emailBookingDTO.setEmail(account.getEmail());
        emailBookingDTO.setAddress(bill.getHouse().getAddress() + ", " + bill.getHouse().getDistricts() + ", " + bill.getHouse().getCity().getName());
        emailBookingDTO.setName(bill.getHouse().getName());
        emailBookingDTO.setPrice(FormatUtils.formatNumber(bill.getPrice()) + " VNĐ");
        emailBookingDTO.setDate(DateUtil.convertDateToString(bill.getCreateDate(), ConstantUtil.DATE_PATTERN));
        emailBookingDTO.setNewValue(String.valueOf(bill.getNewValue()));
        emailBookingDTO.setOldValue(String.valueOf(bill.getOldValue()));
        emailBookingDTO.setType(bill.getType().equals("WATER") ? "Nước" : "Điện");

        return new Thread(() -> {
            try {
                emailService.sendForCheckout(emailBookingDTO, type);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

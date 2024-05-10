package com.example.iHome.controller.front;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.model.dto.FileDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.BookingService;
import com.example.iHome.service.CustomerService;
import com.example.iHome.service.UploadFileService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.ValidatorUtil;
import com.example.iHome.validator.ChangeProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final String DEFAULT_PAGE = "1";

    private static final int PAGE_SIZE = 5;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ChangeProfileValidator changeProfileValidator;

    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication,@RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account owner = customUserDetails.getAccount();
        Customer customer = customerService.findByAccount(owner);

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("date").ascending());
        Page<Booking> bookingList = bookingService.findByCustomer(customer, pageable);

        List<Booking>  bookingListByPage = new ArrayList<>(bookingList.getContent());

        model.addAttribute("bookingDTOList", bookingMapper.toListDTO(bookingListByPage));

        if (action == null) {
            model.addAttribute("messageDTO", null);
        } else {
            model.addAttribute("messageDTO", new MessageDTO(action, status,
                    status.equalsIgnoreCase("success") ? "Đăng ký thành công! Vui lòng đợi chủ nhà chấp nhận!" : "Vui lòng kiểm tra lại thông tin!"));
        }

        model.addAttribute("totalPage", bookingList.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        return "front/profile";
    }
    
    @GetMapping(value = "/booking")
    public String detailBooking(Model model, @RequestParam long id, Authentication authentication) {
        Booking booking = bookingService.findById(id);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account owner = customUserDetails.getAccount();
        Customer customer = customerService.findByAccount(owner);

        if (booking.getCustomer().getId() != customer.getId()) {
            return  "redirect:/login";
        }

        model.addAttribute("bookingDTO", bookingMapper.toDTO(booking));
        return "front/booking_detail";
    }

    @GetMapping(value = "/change")
    public String viewChangeProfile(Model model, Authentication authentication,
                                    @RequestParam(required = false) String action,
                                    @RequestParam(required = false) String status) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();
        Customer customer = customerService.findByAccount(account);

        model.addAttribute("customerDTO", customerMapper.toDTO(customer));
        if (action == null) {
            model.addAttribute("messageDTO", null);
        } else {
            model.addAttribute("messageDTO", new MessageDTO(action, status,
                    status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
        }

        return "front/change_profile";
    }

    @PostMapping(value = "/change")
    public String changeProfile(Model model, CustomerDTO customerDTO, BindingResult bindingResult, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();
        Customer customer;

        changeProfileValidator.validate(customerDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                    "Vui lòng kiểm tra lại thông tin!"));
            model.addAttribute("customerDTO", customerDTO);
            return "front/change_profile";
        }

        account.setFullName(customerDTO.getAccountDTO().getFullName());
        account.setPhone(customerDTO.getAccountDTO().getPhone());
        accountService.save(account);

        if (!ValidatorUtil.isFileEmpty(customerDTO.getAvatarMul())) {
            FileDTO fileDTO = uploadFileService.uploadFile(customerDTO.getAvatarMul(), ConstantUtil.TYPE_UPLOAD_IMAGE);
            customerDTO.setAvatar(fileDTO.getPath());
        }
        if (!ValidatorUtil.isFileEmpty(customerDTO.getIdentityMul())) {
            FileDTO fileDTO = uploadFileService.uploadFile(customerDTO.getIdentityMul(), ConstantUtil.TYPE_UPLOAD_IMAGE);
            customerDTO.setIdentityImage(fileDTO.getPath());
        }

        customer = customerMapper.toEntity(customerDTO);
        customerService.save(customer);

        String redirectUrl = "/profile/change" + "?action=save&status=success";
        return "redirect:" + redirectUrl;
    }

}

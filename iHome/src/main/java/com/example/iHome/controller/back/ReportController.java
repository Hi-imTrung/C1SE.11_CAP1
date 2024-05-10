package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.BillDTO;
import com.example.iHome.model.dto.BookingDTO;
import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.mapper.BillMapper;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.service.BillService;
import com.example.iHome.service.BookingService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.validator.ReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/back/report")
public class ReportController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BillService billService;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private ReportValidator reportValidation;

    @GetMapping("/booking")
    public String showBooking(Model model, Authentication authentication) {

        // default set date == today
        ReportDTO reportDTO = new ReportDTO();
        Date date = new Date();
        String dateStr = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN);
        reportDTO.setStartDate(dateStr);
        reportDTO.setEndDate(dateStr);

        List<BookingDTO> bookingDTOList = getBookingDTOList(authentication, reportDTO);
        setModelReport(model, reportDTO, bookingDTOList);

        return "back/report_booking";
    }

    @PostMapping("/booking/search")
    public String searchReport(Model model, ReportDTO reportDTO, Authentication authentication, BindingResult bindingResult) {
        List<BookingDTO> bookingDTOList;

        reportValidation.validate(reportDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            // if it has error , set date == today
            Date date = new Date();
            String dateStr = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN);
            reportDTO.setStartDate(dateStr);
            reportDTO.setEndDate(dateStr);

            bookingDTOList = getBookingDTOList(authentication, reportDTO);
            setModelReport(model, reportDTO, bookingDTOList);
            return "back/report_booking";
        }

        bookingDTOList = getBookingDTOList(authentication, reportDTO);
        setModelReport(model, reportDTO, bookingDTOList);
        return "back/report_booking";
    }

    @GetMapping("/bill")
    public String showBill(Model model, Authentication authentication) {

        // default set date == today
        ReportDTO reportDTO = new ReportDTO();
        Date date = new Date();
        String dateStr = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN);
        reportDTO.setStartDate(dateStr);
        reportDTO.setEndDate(dateStr);

        List<BillDTO> billDTOList = getBillDTOList(authentication, null, reportDTO);
        setModelReportBill(model, reportDTO, billDTOList);

        return "back/report_bill";
    }

    @PostMapping("/bill/search")
    public String searchReportBill(Model model, ReportDTO reportDTO,
                                   Authentication authentication, BindingResult bindingResult) {
        List<BillDTO> billDTOList;

        reportValidation.validate(reportDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            // if it has error , set date == today
            Date date = new Date();
            String dateStr = DateUtil.convertDateToString(date, ConstantUtil.DATE_PATTERN);
            reportDTO.setStartDate(dateStr);
            reportDTO.setEndDate(dateStr);

            billDTOList = getBillDTOList(authentication, reportDTO.getTypeBill(), reportDTO);
            setModelReportBill(model, reportDTO, billDTOList);
            return "back/report_bill";
        }

        billDTOList = getBillDTOList(authentication, reportDTO.getTypeBill(), reportDTO);
        setModelReportBill(model, reportDTO, billDTOList);
        return "back/report_bill";
    }

    private List<BookingDTO> getBookingDTOList(Authentication authentication, ReportDTO reportDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();
        List<Booking> bookingList;

        if (account.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
            bookingList = bookingService.findReportAndRoleIsAdmin(reportDTO);
        } else {
            bookingList = bookingService.findReport(reportDTO, account.getId());
        }
        return bookingMapper.toListDTO(bookingList);
    }

    private void setModelReport(Model model, ReportDTO reportDTO, List<BookingDTO> bookingDTOList) {
        int countPending = 0;
        int countApproved = 0;

        for (BookingDTO bookingDTO : bookingDTOList) {
            if (bookingDTO.getProgress().equals(ConstantUtil.PROGRESS_PENDING)) {
                countPending += 1;
            } else {
                countApproved += 1;
            }
        }

        reportDTO.setTotalBooking(bookingDTOList.size());
        reportDTO.setTotalPending(countPending);
        reportDTO.setTotalApproved(countApproved);

        model.addAttribute("bookingDTOList", bookingDTOList);
        model.addAttribute("reportDTO", reportDTO);
    }

    private List<BillDTO> getBillDTOList(Authentication authentication, String typeBill, ReportDTO reportDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = customUserDetails.getAccount();
        List<Bill> bookingList;

        if (account.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
            bookingList = billService.findReportAndTypeAndRoleIsAdmin(typeBill, reportDTO);
        } else {
            bookingList = billService.findReportAndType(typeBill, account.getId(), reportDTO);
        }
        return billMapper.toListDTO(bookingList);
    }

    private void setModelReportBill(Model model, ReportDTO reportDTO, List<BillDTO> billDTOList) {
        int countWater = 0;
        int countElectricity = 0;

        for (BillDTO bookingDTO : billDTOList) {
            if (bookingDTO.getType().equals("WATER")) {
                countWater += 1;
            } else {
                countElectricity += 1;
            }
        }

        reportDTO.setTotalBooking(billDTOList.size());
        reportDTO.setTotalPending(countWater);
        reportDTO.setTotalApproved(countElectricity);

        model.addAttribute("billDTOList", billDTOList);
        model.addAttribute("reportDTO", reportDTO);
    }

}

package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.BookingDTO;
import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.mapper.BookingMapper;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.BookingService;
import com.example.iHome.service.CustomerService;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public BookingDTO toDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(DateUtil.convertDateToString(booking.getDate(), ConstantUtil.DATE_PATTERN));
        bookingDTO.setBillImage(booking.getBillImage());
        bookingDTO.setIdentityImage(booking.getIdentityImage());
        bookingDTO.setNote(booking.getNote());
        bookingDTO.setProgress(booking.getProgress());
        bookingDTO.setPrice(FormatUtils.formatNumber(booking.getPrice()));

        // set house
        HouseDTO houseDTO = houseMapper.toDTO(booking.getHouse());
        bookingDTO.setHouseDTO(houseDTO);
        bookingDTO.setHouseId(houseDTO.getId());

        // set customer
        CustomerDTO customerDTO = customerMapper.toDTO(booking.getCustomer());
        bookingDTO.setCustomerDTO(customerDTO);
        bookingDTO.setCustomerId(customerDTO.getId());
        bookingDTO.setPhone(customerDTO.getAccountDTO().getPhone());
        bookingDTO.setFullName(customerDTO.getAccountDTO().getFullName());

        return bookingDTO;
    }

    @Override
    public List<BookingDTO> toListDTO(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }

        List<BookingDTO> result = new ArrayList<>(bookings.size());
        for (Booking booking : bookings) {
            if (booking != null) {
                BookingDTO bookingDTO = toDTO(booking);
                result.add(bookingDTO);
            }
        }

        return result;
    }

    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            return null;
        }

        Booking booking = bookingService.findById(bookingDTO.getId());
        if (booking == null) {
            booking = new Booking();
        }
        booking.setDate(DateUtil.convertStringToDate(bookingDTO.getDate(), ConstantUtil.DATE_PATTERN));
        booking.setNote(bookingDTO.getNote());
        booking.setProgress(bookingDTO.getProgress());
        booking.setPrice(FormatUtils.formatNumber(bookingDTO.getPrice()));
        booking.setFullName(bookingDTO.getFullName());
        booking.setPhone(bookingDTO.getPhone());

        if (bookingDTO.getBillImage() != null) {
            booking.setBillImage(bookingDTO.getBillImage());
        }

        if (bookingDTO.getIdentityImage() != null) {
            booking.setIdentityImage(bookingDTO.getIdentityImage());
        }

        // set house
        booking.setHouse(houseService.findById(bookingDTO.getHouseId()));
        // set customer
        booking.setCustomer(customerService.findById(bookingDTO.getCustomerId()));

        return booking;
    }

}

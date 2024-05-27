package com.example.iHome.service.impl;

import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.House;
import com.example.iHome.repository.BookingRepository;
import com.example.iHome.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Booking> findByCustomer(Customer customer, Pageable pageable) {
        return bookingRepository.findByCustomer(customer, pageable);
    }

    @Override
    public List<Booking> findByOwner(long ownerId) {
        return bookingRepository.findByOwner(ownerId);
    }

    @Override
    public List<Booking> findByHouse(House house) {
        return bookingRepository.findByHouse(house);
    }

    @Override
    public List<Booking> findByHouseAndProgress(House house, String progress) {
        return bookingRepository.findByHouseAndProgress(house, progress);
    }

    @Override
    public List<Booking> findByProgress(String progress) {
        return bookingRepository.findTop10ByProgressOrderByCreatedOnDesc(progress);
    }

    @Override
    public List<Booking> findByProgressAndOwner(String progress, long ownerId) {
        return bookingRepository.findByProgressAndOwner(progress, ownerId);
    }

    @Override
    public List<Booking> findReport(ReportDTO reportDTO, long ownerId) {
        String startDate = reportDTO.getStartDate() + " 00:00:00";
        String endDate = reportDTO.getEndDate() + " 23:59:59";

        return bookingRepository.findReport(startDate, endDate, ownerId);
    }

    @Override
    public List<Booking> findReportAndRoleIsAdmin(ReportDTO reportDTO) {
        String startDate = reportDTO.getStartDate() + " 00:00:00";
        String endDate = reportDTO.getEndDate() + " 23:59:59";
        return bookingRepository.findReportAndRoleIsAdmin(startDate, endDate);
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

}

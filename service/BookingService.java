package com.example.iHome.service;

import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Booking findById(long id);

    Page<Booking> findByCustomer(Customer customer, Pageable pageable);

    List<Booking> findByOwner(long ownerId);

    List<Booking> findByHouse(House house);

    List<Booking> findByHouseAndProgress(House house, String progress);

    List<Booking> findByProgress(String progress);

    List<Booking> findByProgressAndOwner(String progress, long ownerId);

    List<Booking> findReport(ReportDTO reportDTO, long ownerId);

    List<Booking> findReportAndRoleIsAdmin(ReportDTO reportDTO);

    Booking save(Booking booking);

}

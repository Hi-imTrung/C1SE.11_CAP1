package com.example.iHome.service;

import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.House;

import java.util.List;

public interface BillService {

    List<Bill> findAll();

    Bill findById(long id);

    List<Bill> findByOwner(long ownerId);

    List<Bill> findByHouse(House house);

    List<Bill> findReportAndType(String type, long ownerId, ReportDTO reportDTO);

    List<Bill> findReportAndTypeAndRoleIsAdmin(String type, ReportDTO reportDTO);

    Bill save(Bill bill);
}

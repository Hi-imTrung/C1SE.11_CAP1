package com.example.iHome.service.impl;

import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.House;
import com.example.iHome.repository.BillRepository;
import com.example.iHome.service.BillService;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill findById(long id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bill> findByOwner(long ownerId) {
        return billRepository.findByOwner(ownerId);
    }

    @Override
    public List<Bill> findByHouse(House house) {
        return billRepository.findByHouse(house);
    }

    @Override
    public List<Bill> findReportAndType(String type, long ownerId, ReportDTO reportDTO) {
        return ValidatorUtil.isEmpty(type) ? billRepository.findByReport(ownerId, reportDTO.getStartDate(), reportDTO.getEndDate())
                : billRepository.findReportAndType(type, ownerId, reportDTO.getStartDate(), reportDTO.getEndDate());
    }

    @Override
    public List<Bill> findReportAndTypeAndRoleIsAdmin(String type, ReportDTO reportDTO) {
        return ValidatorUtil.isEmpty(type) ? billRepository.findReportAndRoleIsAdmin(reportDTO.getStartDate(), reportDTO.getEndDate())
                : billRepository.findReportAndTypeAndRoleIsAdmin(type, reportDTO.getStartDate(), reportDTO.getEndDate());
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }
}

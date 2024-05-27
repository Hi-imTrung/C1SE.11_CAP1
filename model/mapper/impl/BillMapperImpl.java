package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.BillDTO;
import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.BillMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.BillService;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillMapperImpl implements BillMapper {

    @Autowired
    private BillService billService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BillDTO toDTO(Bill bill) {
        if (bill == null) {
            return null;
        }

        BillDTO billDTO = new BillDTO();
        billDTO.setId(bill.getId());
        billDTO.setTitle(bill.getTitle());
        billDTO.setType(bill.getType());
        billDTO.setCreateDate(DateUtil.convertDateToString(bill.getCreateDate(), ConstantUtil.DATE_PATTERN));
        billDTO.setOldValue(String.valueOf(bill.getOldValue()));
        billDTO.setNewValue(String.valueOf(bill.getNewValue()));
        billDTO.setPrice(FormatUtils.formatNumber(bill.getPrice()));
        billDTO.setStatus(bill.isStatus());

        if (bill.getHouse() != null) {
            House house = bill.getHouse();
            billDTO.setHouseId(house.getId());
            billDTO.setHouseDTO(houseMapper.toDTO(house));
            billDTO.setOwnerName(house.getOwner().getFullName());
        }

        return billDTO;
    }

    @Override
    public List<BillDTO> toListDTO(List<Bill> bills) {
        if (bills == null) {
            return null;
        }
        List<BillDTO> result = new ArrayList<>();

        for (Bill bill : bills) {
            if (bill != null) {
                result.add(toDTO(bill));
            }
        }

        return result;
    }

    @Override
    public Bill toEntity(BillDTO billDTO) {
        if (billDTO == null) {
            return null;
        }

        Bill bill = billService.findById(billDTO.getId());
        if (bill == null) {
            bill = new Bill();
        }
        bill.setTitle(billDTO.getTitle());
        bill.setType(billDTO.getType());
        bill.setCreateDate(DateUtil.convertStringToDate(billDTO.getCreateDate(), ConstantUtil.DATE_PATTERN));
        bill.setOldValue(Double.parseDouble(billDTO.getOldValue()));
        bill.setNewValue(Double.parseDouble(billDTO.getNewValue()));
        bill.setPrice(FormatUtils.formatNumber(billDTO.getPrice()));
        bill.setStatus(billDTO.isStatus());
        bill.setHouse(houseService.findById(billDTO.getHouseId()));

        return bill;
    }
}

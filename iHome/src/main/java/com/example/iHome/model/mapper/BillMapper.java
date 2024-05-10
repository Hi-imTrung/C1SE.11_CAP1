package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.BillDTO;
import com.example.iHome.model.entity.Bill;

import java.util.List;

public interface BillMapper {

    BillDTO toDTO(Bill bill);

    List<BillDTO> toListDTO(List<Bill> bills);

    Bill toEntity(BillDTO billDTO);

}

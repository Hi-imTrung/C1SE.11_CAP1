package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.model.entity.House;

import java.util.List;

public interface HouseMapper {

    HouseDTO toDTO(House house);

    List<HouseDTO> toListDTO(List<House> houses);

    House toEntity(HouseDTO houseDTO);

}

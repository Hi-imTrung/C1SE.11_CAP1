package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.HouseImageDTO;
import com.example.iHome.model.entity.HouseImage;

import java.util.List;

public interface HouseImageMapper {

    HouseImageDTO toDTO(HouseImage houseImage);

    List<HouseImageDTO> toListDTO(List<HouseImage> houseImages);

    HouseImage toEntity(HouseImageDTO houseImageDTO);
    
}

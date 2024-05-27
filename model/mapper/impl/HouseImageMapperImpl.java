package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.HouseImageDTO;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import com.example.iHome.model.mapper.HouseImageMapper;
import com.example.iHome.service.HouseImageService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HouseImageMapperImpl implements HouseImageMapper {

    @Autowired
    private HouseImageService houseImageService;

    @Autowired
    private HouseService houseService;

    @Override
    public HouseImageDTO toDTO(HouseImage houseImage) {
        if (houseImage == null) {
            return null;
        }
        HouseImageDTO houseImageDTO = new HouseImageDTO();
        houseImageDTO.setId(houseImage.getId());
        houseImageDTO.setImageUrl(houseImage.getImageUrl());
        houseImageDTO.setStatus(houseImage.isStatus());
        houseImageDTO.setHouseId(houseImage.getId());

        return houseImageDTO;
    }

    @Override
    public List<HouseImageDTO> toListDTO(List<HouseImage> houseImages) {
        if (houseImages == null) {
            return null;
        }
        List<HouseImageDTO> result = new ArrayList<>(houseImages.size());
        for (HouseImage houseImage : houseImages) {
            if (houseImage != null) {
                HouseImageDTO houseImageDTO = toDTO(houseImage);
                result.add(houseImageDTO);
            }
        }

        return result;
    }

    @Override
    public HouseImage toEntity(HouseImageDTO houseImageDTO) {
        if (houseImageDTO == null) {
            return null;
        }
        HouseImage houseImage = houseImageService.findById(houseImageDTO.getId());
        if (houseImage == null) {
            houseImage = new HouseImage();
        }
        houseImage.setImageUrl(houseImageDTO.getImageUrl());
        houseImage.setStatus(houseImageDTO.isStatus());

        House house = houseService.findById(houseImageDTO.getHouseId());
        houseImage.setHouse(house);

        return houseImage;
    }

}

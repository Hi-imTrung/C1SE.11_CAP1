package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.model.dto.HouseImageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.model.mapper.HouseImageMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.CityService;
import com.example.iHome.service.HouseImageService;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.FormatUtils;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HouseMapperImpl implements HouseMapper {

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseImageService houseImageService;

    @Autowired
    private HouseImageMapper houseImageMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CityService cityService;

    @Override
    public HouseDTO toDTO(House house) {
        if (house == null) {
            return null;
        }

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(house.getId());
        houseDTO.setCreateDate(DateUtil.convertDateToString(house.getCreatedOn(), ConstantUtil.DATE_PATTERN));
        houseDTO.setName(house.getName());
        houseDTO.setCategory(house.getCategory());
        houseDTO.setAddress(house.getAddress());
        houseDTO.setDistricts(house.getDistricts());
        houseDTO.setDescription(house.getDescription());
        houseDTO.setAvatar(house.getAvatar());
        houseDTO.setPrice(FormatUtils.formatNumber(house.getPrice()));
        houseDTO.setBanking(house.getBanking());
        houseDTO.setNumberGuest(house.getNumberGuest());
        houseDTO.setSizeSpace(house.getSizeSpace());
        houseDTO.setRoom(house.getRoom());
        houseDTO.setStatus(house.isStatus());

        // set account
        AccountDTO ownerDTO = accountMapper.toDTO(house.getOwner());
        houseDTO.setOwnerDTO(ownerDTO);
        houseDTO.setOwnerId(ownerDTO.getId());

        // set city
        houseDTO.setCityId(house.getCity().getId());
        houseDTO.setCityName(house.getCity().getName());

        // set list image to house
        List<HouseImage> houseImageList = houseImageService.findByHouse(house);
        List<HouseImageDTO> houseImageDTOList;
        if (houseImageList != null) {
            houseImageDTOList = houseImageMapper.toListDTO(houseImageList);
        } else {
            houseImageDTOList = new ArrayList<>();
        }
        houseDTO.setImageDTOList(houseImageDTOList);

        return houseDTO;
    }

    @Override
    public List<HouseDTO> toListDTO(List<House> houses) {
        if (houses == null) {
            return null;
        }
        List<HouseDTO> result = new ArrayList<>(houses.size());
        for (House house : houses) {
            if (house != null) {
                HouseDTO houseDTO = toDTO(house);
                result.add(houseDTO);
            }
        }

        return result;
    }

    @Override
    public House toEntity(HouseDTO houseDTO) {
        if (houseDTO == null) {
            return null;
        }

        House house = houseService.findById(houseDTO.getId());
        if (house == null) {
            house = new House();
        }
        house.setName(houseDTO.getName());
        house.setCategory(houseDTO.getCategory());
        house.setAddress(houseDTO.getAddress());
        house.setDescription(houseDTO.getDescription());
        house.setDistricts(houseDTO.getDistricts());
        house.setBanking(houseDTO.getBanking());
        house.setPrice(FormatUtils.formatNumber(houseDTO.getPrice()));
        house.setNumberGuest(houseDTO.getNumberGuest());
        house.setSizeSpace(houseDTO.getSizeSpace());
        house.setRoom(houseDTO.getRoom());
        house.setStatus(houseDTO.isStatus());

        if (!ValidatorUtil.isEmpty(houseDTO.getAvatar())) {
            house.setAvatar(houseDTO.getAvatar());
        }

        // set account
        Account owner = accountService.findById(houseDTO.getOwnerId());
        house.setOwner(owner);

        // set city
        house.setCity(cityService.findById(houseDTO.getCityId()));

        return house;
    }

}

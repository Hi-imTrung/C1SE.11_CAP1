package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.CityDTO;
import com.example.iHome.model.entity.City;
import com.example.iHome.model.mapper.CityMapper;
import com.example.iHome.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CityMapperImpl implements CityMapper {

    @Autowired
    private CityService cityService;

    @Override
    public CityDTO toDTO(City city) {
        if (city == null) {
            return null;
        }
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());

        return cityDTO;
    }

    @Override
    public List<CityDTO> toListDTO(List<City> cities) {
        if (cities == null) {
            return null;
        }
        List<CityDTO> result = new ArrayList<>();
        for (City city : cities) {
            if (city != null) {
                CityDTO cityDTO = toDTO(city);
                result.add(cityDTO);
            }
        }
        return result;
    }

    @Override
    public City toEntity(CityDTO cityDTO) {
        if (cityDTO == null) {
            return null;
        }
        City city = cityService.findById(cityDTO.getId());
        city.setName(cityDTO.getName());

        return city;
    }
}

package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.CityDTO;
import com.example.iHome.model.entity.City;

import java.util.List;

public interface CityMapper {

    CityDTO toDTO(City city);

    List<CityDTO> toListDTO(List<City> cities);

    City toEntity(CityDTO cityDTO);
    
}

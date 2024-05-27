package com.example.iHome.service.impl;

import com.example.iHome.model.entity.City;
import com.example.iHome.repository.CityRepository;
import com.example.iHome.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(long id) {
        return cityRepository.findById(id).orElse(null);
    }
}

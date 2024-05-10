package com.example.iHome.service;

import com.example.iHome.model.entity.City;

import java.util.List;

public interface CityService {

    List<City> findAll();

    City findById(long id);

}

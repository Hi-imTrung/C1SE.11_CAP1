package com.example.iHome.service;

import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;

import java.util.List;

public interface HouseImageService {

    List<HouseImage> findAll();

    HouseImage findById(long id);

    List<HouseImage> findByHouse(House house);

    HouseImage save(HouseImage houseImage);

}

package com.example.iHome.service.impl;

import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import com.example.iHome.repository.HouseImageRepository;
import com.example.iHome.service.HouseImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseImageServiceImpl implements HouseImageService {

    @Autowired
    private HouseImageRepository houseImageRepository;

    @Override
    public List<HouseImage> findAll() {
        return houseImageRepository.findAll();
    }

    @Override
    public HouseImage findById(long id) {
        return houseImageRepository.findById(id).orElse(null);
    }

    @Override
    public List<HouseImage> findByHouse(House house) {
        return houseImageRepository.findByHouseAndStatusIsTrue(house);
    }

    @Override
    public HouseImage save(HouseImage houseImage) {
        return houseImageRepository.save(houseImage);
    }
}

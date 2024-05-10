package com.example.iHome.service.impl;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.House;
import com.example.iHome.repository.HouseRepository;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Override
    public List<House> findAll() {
        return houseRepository.findAll();
    }

    @Override
    public House findById(long id) {
        return houseRepository.findById(id).orElse(null);
    }

    @Override
    public List<House> findByOwner(Account owner) {
        return houseRepository.findByOwner(owner);
    }

    @Override
    public Page<House> findByStatusIsTrue(Pageable pageable) {
        return houseRepository.findByStatusIsTrue(pageable);
    }

    @Override
    public Page<House> findByAddressAndCityAndStatusIsTrue(String search, long cityId, Pageable pageable) {
        return houseRepository.findByAddressAndCityAndStatusIsTrue(search, cityId, pageable);
    }

    @Override
    public List<House> findByRelated(long houseId, String districts, int limit) {
        return houseRepository.findByRelated(houseId, districts, limit);
    }

    @Override
    public House save(House house) {
        return houseRepository.save(house);
    }
}

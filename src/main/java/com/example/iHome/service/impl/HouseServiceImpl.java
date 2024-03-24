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
    public List<House> findByName(String name) {
        return houseRepository.findByName(name);
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
    public Page<House> searchByNameAndStatusIsTrue(String search, Pageable pageable) {
        return houseRepository.searchByNameAndStatusIsTrue(search, pageable);
    }

    @Override
    public House save(House house) {
        return houseRepository.save(house);
    }
}

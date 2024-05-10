package com.example.iHome.service;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HouseService {

    List<House> findAll();

    House findById(long id);

    List<House> findByOwner(Account owner);

    Page<House> findByStatusIsTrue(Pageable pageable);

    Page<House> findByAddressAndCityAndStatusIsTrue(String search, long cityId, Pageable pageable);

    List<House> findByRelated(long houseId, String districts, int limit);

    House save(House house);

}

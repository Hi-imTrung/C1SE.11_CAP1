package com.example.iHome.service;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HouseService {

    List<House> findAll();

    House findById(long id);

    List<House> findByName(String name);

    List<House> findByOwner(Account owner);

    Page<House> findByStatusIsTrue(Pageable pageable);

    Page<House> searchByNameAndStatusIsTrue(String search, Pageable pageable);

    House save(House house);

}

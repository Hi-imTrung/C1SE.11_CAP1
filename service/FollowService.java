package com.example.iHome.service;

import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.Follow;

import java.util.List;

public interface FollowService {

    Follow findById(long id);

    List<Follow> findByCustomer(Customer customer);

    Follow save(Follow follow);

}

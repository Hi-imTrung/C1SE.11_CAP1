package com.example.iHome.service.impl;

import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.Follow;
import com.example.iHome.repository.FollowRepository;
import com.example.iHome.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Override
    public Follow findById(long id) {
        return followRepository.findById(id).orElse(null);
    }

    @Override
    public List<Follow> findByCustomer(Customer customer) {
        return followRepository.findByCustomerAndStatusIsTrue(customer);
    }

    @Override
    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }
}

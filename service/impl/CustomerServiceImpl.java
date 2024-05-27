package com.example.iHome.service.impl;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.repository.CustomerRepository;
import com.example.iHome.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer findByAccount(Account account) {
        return customerRepository.findByAccount(account);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}

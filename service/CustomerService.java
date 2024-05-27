package com.example.iHome.service;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Customer findById(long id);

    Customer findByAccount(Account account);

    Customer save(Customer customer);

}

package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setAvatar(customer.getAvatar());
        customerDTO.setIdentityImage(customer.getIdentityImage());

        // set account
        AccountDTO accountDTO = accountMapper.toDTO(customer.getAccount());
        customerDTO.setAccountDTO(accountDTO);
        customerDTO.setFullName(accountDTO.getFullName());
        customerDTO.setAccountId(accountDTO.getId());

        return customerDTO;
    }

    @Override
    public List<CustomerDTO> toListDTO(List<Customer> customers) {
        if (customers == null) {
            return null;
        }

        List<CustomerDTO> result = new ArrayList<>(customers.size());
        for (Customer customer : customers) {
            if (customer != null) {
                CustomerDTO customerDTO = toDTO(customer);
                result.add(customerDTO);
            }
        }

        return result;
    }

    @Override
    public Customer toEntity(CustomerDTO customerDTO) {
        if (customerDTO == null){
            return null;
        }

        Customer customer = customerService.findById(customerDTO.getId());
        if (customer == null) {
            customer = new Customer();
        }
        customer.setAddress(customerDTO.getAddress());

        if (customerDTO.getAvatar() != null) {
            customer.setAvatar(customerDTO.getAvatar());
        }

        if (customerDTO.getIdentityImage() != null) {
            customer.setIdentityImage(customerDTO.getIdentityImage());
        }

        customer.setAccount(accountService.findById(customerDTO.getAccountId()));

        return customer;
    }

}

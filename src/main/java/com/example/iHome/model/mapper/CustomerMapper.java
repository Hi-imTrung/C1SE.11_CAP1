package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.model.entity.Customer;

import java.util.List;

public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);

    List<CustomerDTO> toListDTO(List<Customer> customers);

    Customer toEntity(CustomerDTO customerDTO);

}

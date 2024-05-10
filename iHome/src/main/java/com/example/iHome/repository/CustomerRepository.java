package com.example.iHome.repository;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByAccount(Account account);

}

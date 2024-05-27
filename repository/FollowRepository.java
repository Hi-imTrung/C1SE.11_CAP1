package com.example.iHome.repository;

import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByCustomerAndStatusIsTrue(Customer customer);

}

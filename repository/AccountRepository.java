package com.example.iHome.repository;

import com.example.iHome.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailAndStatusIsTrue(String email);

    Account findByEmail(String email);

    Account findByPhone(String phone);

}

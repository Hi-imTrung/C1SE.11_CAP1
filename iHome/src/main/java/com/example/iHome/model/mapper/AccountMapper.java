package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.entity.Account;

import java.util.List;

public interface AccountMapper {

    AccountDTO toDTO(Account account);

    List<AccountDTO> toListDTO(List<Account> accounts);

    Account toEntity(AccountDTO accountDTO);

}

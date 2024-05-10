package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Role;
import com.example.iHome.model.mapper.AccountMapper;
import com.example.iHome.model.mapper.RoleMapper;
import com.example.iHome.service.AccountService;
import com.example.iHome.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setFullName(account.getFullName());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPhone(account.getPhone());
        accountDTO.setStatus(account.isStatus());

        Role role = account.getRole();
        if (!ObjectUtils.isEmpty(role)) {
            accountDTO.setRoleId(role.getId());
            accountDTO.setRoleDTO(roleMapper.toDTO(account.getRole()));
            accountDTO.setRoleName(role.getName());
        }

        return accountDTO;
    }

    @Override
    public List<AccountDTO> toListDTO(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }

        List<AccountDTO> result = new ArrayList<>(accounts.size());
        for (Account account : accounts) {
            if (account != null) {
                AccountDTO accountDTO = toDTO(account);
                result.add(accountDTO);
            }
        }

        return result;
    }

    @Override
    public Account toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }

        Account account = accountService.findById(accountDTO.getId());

        if (account == null) {
            account = new Account();
        }
        account.setFullName(accountDTO.getFullName());
        account.setEmail(accountDTO.getEmail());
        account.setPhone(accountDTO.getPhone());
        account.setStatus(accountDTO.isStatus());
        if (accountDTO.getRoleId() != 0) {
            account.setRole(roleService.findById(accountDTO.getRoleId()));
        }

        return account;
    }
}

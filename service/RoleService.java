package com.example.iHome.service;

import com.example.iHome.model.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findById(long id);

    Role findByName(String name);

    Role save(Role role);

}

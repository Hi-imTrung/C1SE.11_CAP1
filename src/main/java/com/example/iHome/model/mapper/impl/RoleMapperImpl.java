package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.RoleDTO;
import com.example.iHome.model.entity.Role;
import com.example.iHome.model.mapper.RoleMapper;
import com.example.iHome.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Autowired
    private RoleService roleService;

    @Override
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setStatus(role.isStatus());

        return roleDTO;
    }

    @Override
    public List<RoleDTO> toListDTO(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        List<RoleDTO> result = new ArrayList<>(roles.size());

        for (Role role : roles) {
            if (role != null) {
                RoleDTO roleDTO = toDTO(role);
                result.add(roleDTO);
            }
        }

        return result;
    }

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        Role role = roleService.findById(roleDTO.getId());

        if (role == null) {
            role = new Role();
        }
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setStatus(roleDTO.isStatus());

        return role;
    }
}

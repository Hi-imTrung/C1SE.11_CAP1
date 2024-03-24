package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.RoleDTO;
import com.example.iHome.model.entity.Role;

import java.util.List;

public interface RoleMapper {

    RoleDTO toDTO(Role role);

    List<RoleDTO> toListDTO(List<Role> roles);

    Role toEntity(RoleDTO roleDTO);
    
}

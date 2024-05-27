package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.FollowDTO;
import com.example.iHome.model.entity.Follow;

import java.util.List;

public interface FollowMapper {

    FollowDTO toDTO(Follow follow);

    List<FollowDTO> toListDTO(List<Follow> follows);

    Follow toEntity(FollowDTO followDTO);

}

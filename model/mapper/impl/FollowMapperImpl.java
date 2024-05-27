package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.model.dto.FollowDTO;
import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.model.entity.Follow;
import com.example.iHome.model.mapper.CustomerMapper;
import com.example.iHome.model.mapper.FollowMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.CustomerService;
import com.example.iHome.service.FollowService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FollowMapperImpl implements FollowMapper {

    @Autowired
    private FollowService followService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public FollowDTO toDTO(Follow follow) {
        if (follow == null) {
            return null;
        }
        FollowDTO followDTO = new FollowDTO();

        followDTO.setId(follow.getId());
        followDTO.setStatus(follow.isStatus());

        HouseDTO houseDTO = houseMapper.toDTO(houseService.findById(follow.getHouse().getId()));
        followDTO.setHouseDTO(houseDTO);
        followDTO.setHouseId(houseDTO.getId());

        CustomerDTO customerDTO = customerMapper.toDTO(customerService.findById(follow.getCustomer().getId()));
        followDTO.setCustomerDTO(customerDTO);
        followDTO.setCustomerId(customerDTO.getId());

        return followDTO;
    }

    @Override
    public List<FollowDTO> toListDTO(List<Follow> follows) {
        if (follows == null) {
            return null;
        }
        List<FollowDTO> result = new ArrayList<>();
        for (Follow follow : follows) {
            if (follow != null) {
                result.add(toDTO(follow));
            }
        }

        return result;
    }

    @Override
    public Follow toEntity(FollowDTO followDTO) {
        if (followDTO == null) {
            return null;
        }
        Follow follow = followService.findById(followDTO.getId());

        follow.setId(followDTO.getId());
        follow.setStatus(followDTO.isStatus());
        follow.setHouse(houseService.findById(followDTO.getHouseId()));
        follow.setCustomer(customerService.findById(followDTO.getCustomerId()));

        return follow;
    }

}

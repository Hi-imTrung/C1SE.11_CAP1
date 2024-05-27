package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDTO {

    private long id;

    private HouseDTO houseDTO;
    private long houseId;

    private CustomerDTO customerDTO;
    private long customerId;
    private boolean status;

}

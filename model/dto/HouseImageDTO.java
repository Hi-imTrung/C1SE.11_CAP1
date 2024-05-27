package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseImageDTO {

    private long id;
    private String imageUrl;
    private boolean status;

    private long houseId;
    private HouseDTO houseDTO;

}

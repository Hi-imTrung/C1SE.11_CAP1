package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDTO {

    private long id;
    private String title;
    private String type;
    private String createDate;
    private String newValue;
    private String oldValue;
    private String price;
    private boolean status;

    private long houseId;
    private HouseDTO houseDTO;

    private String ownerName;
}

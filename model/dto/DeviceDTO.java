package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DeviceDTO {

    private long id;
    private String name;
    private String description;
    private String progress;
    private String image;
    private int amount;
    private boolean status;

    private HouseDTO houseDTO;
    private long houseId;

    private MultipartFile imageMul;

}

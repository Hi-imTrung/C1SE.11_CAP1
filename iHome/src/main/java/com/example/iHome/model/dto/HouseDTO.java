package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class HouseDTO {

    private long id;
    private String createDate;
    private String name;
    private String category;
    private String address;
    private String districts;
    private String description;
    private String avatar;
    private String price;
    private String banking;
    private int numberGuest;
    private int sizeSpace;
    private int room;
    private boolean status;

    private AccountDTO ownerDTO;
    private long ownerId;

    private long cityId;
    private String cityName;

    private List<HouseImageDTO> imageDTOList;

    private MultipartFile avatarMul;

}

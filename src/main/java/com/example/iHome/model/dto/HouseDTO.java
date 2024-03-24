package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class HouseDTO {

    private long id;
    private String name;
    private String address;
    private String description;
    private String avatar;
    private String price;
    private boolean status;

    private AccountDTO ownerDTO;
    private long ownerId;

    private List<HouseImageDTO> imageDTOList;

    private MultipartFile avatarMul;

}

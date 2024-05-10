package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BookingDTO {

    private long id;
    private String fullName;
    private String phone;
    private String date;
    private String progress;
    private String note;
    private String billImage;
    private String price;
    private String identityImage;

    private CustomerDTO customerDTO;
    private long customerId;

    private HouseDTO houseDTO;
    private long houseId;

    private MultipartFile billMul;

}

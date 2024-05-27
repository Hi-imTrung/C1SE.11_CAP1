package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CustomerDTO {

    private long id;
    private String address;
    private String avatar;
    private String identityImage;

    private AccountDTO accountDTO;
    private String fullName;
    private String phone;
    private long accountId;

    private MultipartFile identityMul;

    private MultipartFile avatarMul;
}

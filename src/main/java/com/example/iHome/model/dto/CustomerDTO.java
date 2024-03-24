package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    private long id;
    private String address;
    private String avatar;
    private String identityImage;

    private AccountDTO accountDTO;
    private String fullName;
    private long accountId;

}

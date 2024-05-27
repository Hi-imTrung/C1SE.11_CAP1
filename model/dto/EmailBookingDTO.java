package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EmailBookingDTO {
    private long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;

    private String name;
    private String address;
    private String price;
    private String date;

    private String newValue;
    private String oldValue;
    private String type;

    private Map<String, Object> properties;
}

package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EmailTemplateDTO {

    private long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;

    private Map<String, Object> properties;

}

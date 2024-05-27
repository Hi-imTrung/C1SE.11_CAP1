package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private boolean status;

    private RoleDTO roleDTO;
    private long roleId;
    private String roleName;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}

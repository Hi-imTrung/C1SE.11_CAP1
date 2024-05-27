package com.example.iHome.validator;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.service.AccountService;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    private static final int LENGTH_EMAIL = 150;

    private static final int LENGTH_FULL_NAME = 50;

    @Autowired
    private AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) target;

        // verify fullName
        if (ValidatorUtil.isEmpty(accountDTO.getFullName().trim())) {
            errors.rejectValue("fullName", "Vui lòng nhập Họ và Tên!",
                    "Vui lòng nhập Họ và Tên!");
        } else if (ValidatorUtil.isName(accountDTO.getFullName())) {
            errors.rejectValue("fullName", "Họ và Tên không được chứa số!",
                    "Họ và Tên không được chứa số!");
        } else if (accountDTO.getFullName().length() > LENGTH_FULL_NAME){
            errors.rejectValue("fullName","Tên không được vượt quá 50 kí tự",
                    "Tên không được vượt quá 50 kí tự");
        }

        // verify email
        if (ValidatorUtil.isEmpty(accountDTO.getEmail().trim())) {
            errors.rejectValue("email", "Vui lòng nhập Email", "Vui lòng nhập Email");
        } else {
            if (!ValidatorUtil.isEmail(accountDTO.getEmail())) {
                errors.rejectValue("email", "Vui lòng nhập Email đúng định dạng",
                        "Vui lòng nhập Email đúng định dạng");
            } else if (accountDTO.getEmail().length() > LENGTH_EMAIL){
                errors.rejectValue("email","Email không được vượt quá 150 kí tự",
                        "Tên không được vượt quá 150 kí tự");
            } else {
                Account account = accountService.findByEmail(accountDTO.getEmail());
                if (account != null && account.getId() != accountDTO.getId()) {
                    errors.rejectValue("email", "Email đã được đăng ký!",
                            "Email đã được đăng ký!");
                }
            }
        }

        // verify phone
        if (ValidatorUtil.isEmpty(accountDTO.getPhone().trim())) {
            errors.rejectValue("phone", "Vui lòng nhập Số Điện Thoại!",
                    "Vui lòng nhập Số Điện Thoại!");
        } else {
            if (!ValidatorUtil.isPhoneNumber(accountDTO.getPhone())) {
                errors.rejectValue("phone", "Vui lòng nhập đúng định dạng", "Vui lòng nhập đúng định dạng");
            }
            else {
                Account account = accountService.findByPhone(accountDTO.getPhone());
                if (account != null && account.getId() != accountDTO.getId()) {
                    errors.rejectValue("phone", "Số Điện Thoại đã được đăng ký!",
                            "Số Điện Thoại đã được đăng ký!");
                }
            }
        }

    }
}

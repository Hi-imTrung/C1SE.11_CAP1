package com.example.iHome.validator;

import com.example.iHome.model.dto.CustomerDTO;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangeProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDTO customerDTO = (CustomerDTO) target;

        if (ValidatorUtil.isEmpty(customerDTO.getAccountDTO().getFullName())) {
            errors.rejectValue("fullName", "Vui lòng nhập Họ & Tên!", "Vui lòng nhập Họ & Tên!");
        }

        if (ValidatorUtil.isEmpty(customerDTO.getAccountDTO().getPhone())) {
            errors.rejectValue("phone", "Vui lòng nhập Số Điện Thoại!", "Vui lòng nhập Số Điện Thoại!");
        }

    }

}

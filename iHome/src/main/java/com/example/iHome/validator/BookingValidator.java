package com.example.iHome.validator;

import com.example.iHome.model.dto.BookingDTO;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookingDTO bookingDTO = (BookingDTO) target;

        if (ValidatorUtil.isEmpty(bookingDTO.getFullName())) {
            errors.rejectValue("fullName", "Vui lòng nhập Họ & Tên", "Vui lòng nhập Họ & Tên");
        }

        if (ValidatorUtil.isEmpty(bookingDTO.getPhone())) {
            errors.rejectValue("phone", "Vui lòng nhập Số điện thoại", "Vui lòng nhập Số điện thoại");
        }

    }
}

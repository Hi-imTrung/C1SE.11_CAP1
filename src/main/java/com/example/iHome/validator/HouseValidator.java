package com.example.iHome.validator;
import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class HouseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return HouseDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HouseDTO houseDTO = (HouseDTO) target;

        //verify name
        if (ValidatorUtil.isEmpty(houseDTO.getName())) {
            errors.rejectValue("name", "Vui lòng nhập Tên Phòng Trọ!",
                    "Vui lòng nhập Tên Phòng Trọ!");
        }

        //verify address
        if (ValidatorUtil.isEmpty(houseDTO.getAddress())) {
            errors.rejectValue("address", "Vui lòng nhập Địa Chỉ!",
                    "Vui lòng nhập Địa Chỉ!");
        }

        //verify description
        if (ValidatorUtil.isEmpty(houseDTO.getDescription())) {
            errors.rejectValue("description", "Vui lòng nhập Thông Tin Chi Tiết!",
                    "Vui lòng nhập Thông Tin Chi Tiết!");
        }

        //verify price
        if (ValidatorUtil.isEmpty(houseDTO.getPrice())) {
            errors.rejectValue("price", "Vui lòng nhập Giá Tiền!",
                    "Vui lòng nhập Giá Tiền!");
        } else {
            if (!ValidatorUtil.isNumeric(houseDTO.getPrice())) {
                errors.rejectValue("price", "Giá Tiền phải là kiểu số!",
                        "Giá Tiền phải là kiểu số!");
            }
        }

    }

}

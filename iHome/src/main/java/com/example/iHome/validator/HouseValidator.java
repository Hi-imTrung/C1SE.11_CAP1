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

        //verify districts
        if (ValidatorUtil.isEmpty(houseDTO.getDistricts())) {
            errors.rejectValue("districts", "Vui lòng nhập Quận / Huyện!",
                    "Vui lòng nhập Quận / Huyện!");
        }

        //verify banking
        if (ValidatorUtil.isEmpty(houseDTO.getBanking())) {
            errors.rejectValue("banking", "Vui lòng nhập Tài Khoản Thanh Toán!",
                    "Vui lòng nhập Tài Khoản Thanh Toán!");
        }

        //verify room
        if (!ValidatorUtil.isNumeric(String.valueOf(houseDTO.getRoom()))) {
            errors.rejectValue("room", "Số Phòng phải là kiểu số!",
                    "Số Phòng phải là kiểu số!");
        }

        //verify size space
        if (!ValidatorUtil.isNumeric(String.valueOf(houseDTO.getSizeSpace()))) {
            errors.rejectValue("sizeSpace", "Diện tích phải là kiểu số!",
                    "Diện tích phải là kiểu số!");
        }

        //verify number guest
        if (!ValidatorUtil.isNumeric(String.valueOf(houseDTO.getNumberGuest()))) {
            errors.rejectValue("numberGuest", "Giá Tiền phải là kiểu số!",
                    "Giá Tiền phải là kiểu số!");
        }

    }

}

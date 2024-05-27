package com.example.iHome.validator;

import com.example.iHome.model.dto.DeviceDTO;
import com.example.iHome.util.ValidatorUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DeviceDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DeviceDTO deviceDTO = (DeviceDTO) target;

        //verify name
        if (ValidatorUtil.isEmpty(deviceDTO.getName())) {
            errors.rejectValue("name", "Vui lòng nhập Tên Thiết Bị!",
                    "Vui lòng nhập Tên Thiết Bị!");
        }

        //verify description
        if (ValidatorUtil.isEmpty(deviceDTO.getDescription())) {
            errors.rejectValue("description", "Vui lòng nhập Mô Tả Chi Tiết!",
                    "Vui lòng nhập Mô Tả Chi Tiết!");
        }

        //verify amount
        if (ValidatorUtil.isEmpty(String.valueOf(deviceDTO.getAmount()))) {
            errors.rejectValue("amount", "Vui lòng nhập Số Lượng!",
                    "Vui lòng nhập Số Lượng!");
        } else {
            if (!ValidatorUtil.isNumeric(String.valueOf(deviceDTO.getAmount()))) {
                errors.rejectValue("amount", "Số Lượng phải là số!",
                        "Số Lượng phải là số!");
            }
        }

        //verify progress
        if (ValidatorUtil.isEmpty(deviceDTO.getProgress())) {
            errors.rejectValue("progress", "Vui lòng nhập Tình Trạng!",
                    "Vui lòng nhập Tình Trạng!");
        }

        //verify house name
        if (deviceDTO.getHouseId() == 0) {
            errors.rejectValue("houseId", "Vui lòng chọn Tên Nhà Trọ!",
                    "Vui lòng chọn Tên Nhà Trọ!");
        }

    }

}

package com.example.iHome.validator;

import com.example.iHome.model.dto.BillDTO;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class BillValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BillDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BillDTO billDTO = (BillDTO) target;

        //verify title
        if (ValidatorUtil.isEmpty(billDTO.getTitle())) {
            errors.rejectValue("title", "Vui lòng nhập Tên Hóa Đơn!",
                    "Vui lòng nhập Tên Hóa Đơn!");
        }

        //verify houseId
        if (billDTO.getHouseId() == 0) {
            errors.rejectValue("houseId", "Vui lòng chọn Nhà Trọ!",
                    "Vui lòng chọn Nhà Trọ!");
        }

        //verify create date
        if (billDTO.getCreateDate() != null && !billDTO.getCreateDate().trim().isEmpty()) {
            Date dateStart = DateUtil.convertStringToDate(billDTO.getCreateDate(), ConstantUtil.DATE_PATTERN);
            Date dateEnd = new Date();
            if (dateStart.compareTo(dateEnd) > 0) {
                errors.rejectValue("createDate", "Ngày chọn lớn hơn ngày hiện tại!",
                        "Ngày chọn lớn hơn ngày hiện tại!");
            }
        }

        //verify price
        if (ValidatorUtil.isEmpty(billDTO.getPrice())) {
            errors.rejectValue("price", "Vui lòng nhập Giá Tiền!",
                    "Vui lòng nhập Giá Tiền!");
        } else {
            if (!ValidatorUtil.isNumeric(billDTO.getPrice())) {
                errors.rejectValue("price", "Giá Tiền phải là kiểu số!",
                        "Giá Tiền phải là kiểu số!");
            }
        }

        //verify new value
        if (ValidatorUtil.isEmpty(billDTO.getNewValue())) {
            errors.rejectValue("newValue", "Vui lòng nhập Giá Trị Mới!",
                    "Vui lòng nhập Giá Trị Mới!");
        } else {
            if (!ValidatorUtil.isNumeric(billDTO.getNewValue())) {
                errors.rejectValue("newValue", "Giá Trị Mới phải là kiểu số!",
                        "Giá Trị Mới phải là kiểu số!");
            }
        }

        //verify old value
        if (ValidatorUtil.isEmpty(billDTO.getOldValue())) {
            errors.rejectValue("oldValue", "Vui lòng nhập Giá Trị Cũ!",
                    "Vui lòng nhập Giá Trị Cũ!");
        } else {
            if (!ValidatorUtil.isNumeric(billDTO.getOldValue())) {
                errors.rejectValue("oldValue", "Giá Trị Cũ phải là kiểu số!",
                        "Giá Trị Cũ phải là kiểu số!");
            }
        }

    }

}

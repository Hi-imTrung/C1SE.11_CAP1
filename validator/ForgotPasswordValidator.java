package com.example.iHome.validator;

import com.example.iHome.model.dto.AccountDTO;
import com.example.iHome.util.ValidatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ForgotPasswordValidator implements Validator {

    private static final int LENGTH_PASSWORD = 6;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) target;
        String newPassword = accountDTO.getNewPassword();
        String confirmPassword = accountDTO.getConfirmPassword();

        if (ValidatorUtil.isEmpty(newPassword)) {
            errors.rejectValue("newPassword", "Vui lòng nhập mật khẩu mới!", "Vui lòng nhập mật khẩu mới!");
        } else if (newPassword.length() < LENGTH_PASSWORD) {
            errors.rejectValue("newPassword", "Mật khẩu cần ít nhất 6 ký tự!", "Mật khẩu cần ít nhất 6 ký tự!");
        }

        if (ValidatorUtil.isEmpty(confirmPassword)) {
            errors.rejectValue("confirmPassword", "Vui lòng nhập xác nhận mật khẩu mới!",
                    "Vui lòng nhập xác nhận mật khẩu mới!");
        } else if (confirmPassword.length() < LENGTH_PASSWORD) {
            errors.rejectValue("confirmPassword", "Xác nhận mật khẩu cần ít nhất 6 ký tự!",
                    "Xác nhận mật khẩu cần ít nhất 6 ký tự!");
        } else {
            if (!newPassword.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword", "Mật khẩu không trùng khớp!",
                        "Mật khẩu không trùng khớp!");
            }
        }

    }

}

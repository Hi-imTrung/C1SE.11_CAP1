package com.example.iHome.validator;

import com.example.iHome.model.dto.ReportDTO;
import com.example.iHome.util.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class ReportValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ReportDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReportDTO reportDTO = (ReportDTO) target;

        if (reportDTO.getStartDate() != null && !reportDTO.getStartDate().trim().isEmpty() &&
                reportDTO.getEndDate() != null && !reportDTO.getEndDate().trim().isEmpty()) {
            Date dateStart = DateUtil.convertStringToDate(reportDTO.getStartDate(), "yyyy-MM-dd");
            Date dateEnd = DateUtil.convertStringToDate(reportDTO.getEndDate(), "yyyy-MM-dd");
            if (dateStart.compareTo(dateEnd) > 0) {
                errors.rejectValue("startDate", "Thời gian bắt đầu lớn hơn thời gian kết thúc",
                        "Thời gian bắt đầu lớn hơn thời gian kết thúc");
            }
        }

    }
}

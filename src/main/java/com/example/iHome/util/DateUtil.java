package com.example.iHome.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd";

    public boolean compareDate(String endDate, String startDate) {
        if (!endDate.isEmpty() || !startDate.isEmpty()) {
            Date end = convertStringToDate(endDate, PATTERN);
            Date start = convertStringToDate(startDate, PATTERN);
            return start != null && start.before(end);
        }
        return false;
    }

    public static Date convertStringToDate(String value, String pattern) {
        try {
            if (value != null) {
                return new SimpleDateFormat(pattern).parse(value);
            }
        } catch (ParseException e) {
            return null;
        }
        return null;
    }

    public static String convertDateToString(Date value, String pattern) {
        return new SimpleDateFormat(pattern).format(value);
    }

}

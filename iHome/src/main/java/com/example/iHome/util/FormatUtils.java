package com.example.iHome.util;

import java.text.DecimalFormat;

public class FormatUtils {

    private static final DecimalFormat df = new DecimalFormat("###,###,###");

    public static String formatNumber(double value) {
        try {
            String result = df.format(value);
            return result.startsWith(".") ? "0" + result : result;
        } catch (Exception ex) {
            return "";
        }
    }

    public static double formatNumber(String value) {
        try {
            String target = value.replaceAll(",", "").trim();
            return Double.parseDouble(target);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String toEncodePrice(String input) {
        return input.replace(",", "");
    }

}

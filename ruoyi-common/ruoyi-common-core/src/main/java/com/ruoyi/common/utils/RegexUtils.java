package com.ruoyi.common.utils;


import java.util.regex.Pattern;

public class RegexUtils {

    /**
     *  正则：手机号
     * @param in
     * @return
     */
    public static boolean validateMobilePhone(String in) {
        Pattern pattern = Pattern.compile("^1[3456789]\\d{9}$");
        if (in.trim().length() != 11){
            return false;
        }
        return pattern.matcher(in.trim()).matches();
    }
}
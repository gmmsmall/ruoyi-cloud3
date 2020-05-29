package com.ruoyi.javamail.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailEncryptUtil {
    private static EncryptUtil encryptUtil;

    static {
        try {
            encryptUtil = new EncryptUtil("DB2cBejzKk9HvzrKRIM3uGNx4KWYy0dBJ0nYvlDgPd4xwPvkml");
        } catch (Exception e) {
            log.error("encryptUtil error");
        }
    }

    public static EncryptUtil getInstance() {
        return encryptUtil;
    }
}

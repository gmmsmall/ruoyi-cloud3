package com.ruoyi.javamail.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PwdEncryptUtil {
    private static EncryptUtil encryptUtil;

    static {
        try {
            encryptUtil = new EncryptUtil("DRvHqRv9fRBSgxoIOeZiBJVOcqfZeqMGab28FNQcPClRF47f6x");
        } catch (Exception e) {
            log.error("encryptUtil error");
        }
    }

    public static EncryptUtil getInstance() {
        return encryptUtil;
    }
}

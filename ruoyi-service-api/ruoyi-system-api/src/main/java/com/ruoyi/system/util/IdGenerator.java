package com.ruoyi.system.util;

import org.apache.commons.lang3.RandomStringUtils;

public class IdGenerator {
    public static final String getId() {
        return System.currentTimeMillis() + RandomStringUtils.randomNumeric(6);
    }
}

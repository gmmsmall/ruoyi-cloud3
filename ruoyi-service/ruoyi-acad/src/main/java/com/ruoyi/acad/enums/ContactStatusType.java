package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 联络状态枚举
 * @Author guomiaomiao
 * @Date 2020/7/2 10:05
 * @Version 1.0
 */
@Getter
public enum ContactStatusType {

    COMMUNICATION("1","已通讯"),
    VISIT("2","已到访"),
    SIGN("3","已签约"),
    NOCOMM("0","未通讯")
    ;
    private String type;
    private String desc;

    ContactStatusType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ContactStatusType of(String type) {
        if(type == null || StringUtils.isEmpty(type) || type.equals("null") || type.equals("4")){
            type = "0";//默认是未通讯
        }
        for (ContactStatusType gender : ContactStatusType.values()) {
            if (gender.type.equals(type)) {
                return gender;
            }
        }
        throw new NullPointerException();
    }
}

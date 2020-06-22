package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 联络方式枚举
 * @Author guomiaomiao
 * @Date 2020/6/18 16:05
 * @Version 1.0
 */
@Getter
public enum ContactMethonType {

    EMAIL("1","邮箱"),
    PHONE("2","电话"),
    MORE("3","邮箱/电话"),
    NOCONTRACT("4","未联络")
    ;
    private String type;
    private String desc;

    ContactMethonType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ContactMethonType of(String type) {
        if(type == null || StringUtils.isEmpty(type) || type.equals("null") || type.equals("0")){
            type = "4";//默认是未联络
        }
        for (ContactMethonType gender : ContactMethonType.values()) {
            if (gender.type.equals(type)) {
                return gender;
            }
        }
        throw new NullPointerException();
    }
}

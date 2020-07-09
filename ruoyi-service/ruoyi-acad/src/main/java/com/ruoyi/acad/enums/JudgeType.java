package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 是否枚举
 * @Author guomiaomiao
 * @Date 2020/6/18 14:43
 * @Version 1.0
 */
@Getter
public enum JudgeType {

    YES("1","是"), NO("0", "否");
    private String type;
    private String desc;

    JudgeType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static JudgeType of(String type) {
        if(type == null || StringUtils.isEmpty(type) || type.equals("null")){
            type = "0";//默认是否
        }else if(type.equals("true")){
            type = "1";
        }else{
            type = "0";
        }
        for (JudgeType gender : JudgeType.values()) {
            if (gender.type.equals(type)) {
                return gender;
            }
        }
        throw new NullPointerException();
    }

}

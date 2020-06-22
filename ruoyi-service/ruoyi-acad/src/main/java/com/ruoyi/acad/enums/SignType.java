package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 签约类型枚举
 * @Author guomiaomiao
 * @Date 2020/6/18 16:08
 * @Version 1.0
 */
@Getter
public enum SignType {

    FULLTIME("1","全职"),
    RIGIDITY("2","刚性"),
    FLEXIBILITY("3","柔性"),
    REGISTER("4","注册"),
    OTHERS("5","其他")
    ;
    private String type;
    private String desc;

    SignType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static SignType of(String type) {
        if(type == null || StringUtils.isEmpty(type) || type.equals("null") || type.equals("0")){
            type = "5";//默认是其他
        }
        for (SignType gender : SignType.values()) {
            if (gender.type.equals(type)) {
                return gender;
            }
        }
        throw new NullPointerException();
    }
}

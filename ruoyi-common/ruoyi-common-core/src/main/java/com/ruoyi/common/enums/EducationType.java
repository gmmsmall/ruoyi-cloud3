package com.ruoyi.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;

/**
 * 教育分类
 * @Author guomiaomiao
 * @Date 2020/6/11 13:19
 * @Version 1.0
 */
@ApiModel(value = "教育分类，1-学士，2-硕士，3-博士，4-未知")
public enum EducationType {

    BACHELOR(1,"学士"),
    MASTER(2,"硕士"),
    DOCTOR(3,"博士"),
    UNKNOWN(4,"未知"),
    ;

    private Integer code;

    private String msg;

    EducationType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据代码获取枚举
     * 不要尝试缓存全部的枚举，该方法用到的频率不会太高，且枚举很少，不会造成资源浪费
     *
     * 使用 @JsonCreator 让 jackson 解析 json 的时候能匹配到该枚举
     * 参考：https://segmentfault.com/q/1010000020636087
     * @param code
     * @return
     */
    @JsonCreator
    public static EducationType getByCode(int code) {

        EducationType[] values = EducationType.values();

        for (EducationType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}

package com.ruoyi.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;

/**
 * 研究领域分类
 * @Author guomiaomiao
 * @Date 2020/6/11 10:47
 * @Version 1.0
 */
@ApiModel(value = "研究领域分类，八大类专业领域1-高端装备制造，2-生物医药，3-新能源新材料，4-网络信息，5-设计研发，6-海洋经济，7-军民融合，8-其他")
public enum RsfCategoryType {

    HIFHEQU(1,"高端装备制造"),
    BIOSCIENCE(2,"生物医药"),
    NEWENERGY(3,"新能源新材料"),
    NETWORKINFO(4,"网络信息"),
    DESIGNDEV(5,"设计研发"),
    MARINEECO(6,"海洋经济"),
    ARMYCIVIL(7,"军民融合"),
    OTHER(8,"其他")
    ;

    private Integer code;

    private String msg;

    RsfCategoryType(Integer code,String msg){
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
    public static RsfCategoryType getByCode(int code) {

        RsfCategoryType[] values = RsfCategoryType.values();

        for (RsfCategoryType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}

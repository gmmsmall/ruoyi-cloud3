package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 研究领域枚举
 * @Author guomiaomiao
 * @Date 2020/6/18 15:52
 * @Version 1.0
 */
@Getter
public enum RsfCategoryType {

    EQUIPMENT("1","高端装备制造"),
    BIOMEDICINE("2","生物医药"),
    ENERGY("3","新能源新材料"),
    NETWORK("4","网络信息"),
    DEVELOPMENT("5","设计研发"),
    ECONOMY("6","海洋经济"),
    CIVIL("7","军民融合"),
    OTHER("8", "其他");
    private String type;
    private String desc;

    RsfCategoryType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static RsfCategoryType of(String type) {
        if(type == null || StringUtils.isEmpty(type) || type.equals("null") || type.equals("0")){
            type = "8";//默认是其他
        }
        for (RsfCategoryType gender : RsfCategoryType.values()) {
            if (gender.type.equals(type)) {
                return gender;
            }
        }
        throw new NullPointerException();
    }
}

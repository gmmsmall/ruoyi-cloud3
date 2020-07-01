package com.ruoyi.acad.enums;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;

/**
 * 论文级别枚举
 * @Author guomiaomiao
 * @Date 2020/7/1 16:18
 * @Version 1.0
 */
@Getter
public enum PeriodicalType {

    SCI(1,"SCI"),
    EI(2,"EI"),
    IEEE(3,"IEEE"),
    OTHER(4,"其他"),
    ;
    private int type;
    private String desc;

    PeriodicalType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static PeriodicalType of(int type) {
        if(type == 0){
            type = 4;//默认是其他
        }
        for (PeriodicalType gender : PeriodicalType.values()) {
            if (gender.type == type) {
                return gender;
            }
        }
        throw new NullPointerException();
    }
}

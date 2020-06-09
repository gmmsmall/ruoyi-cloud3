package com.ruoyi.common.log.enums;

import io.swagger.annotations.ApiModel;

/**
 * 世界各大洲枚举
 * @Author guomiaomiao
 * @Date 2020/6/5 17:41
 * @Version 1.0
 */
@ApiModel(value = "com.ruoyi.common.log.enums.CountryType",description = "世界各大洲枚举:AS-亚洲,EU-欧洲,NA-北美洲,SA-南美洲,OA-大洋洲,AF-非洲,AN-南极洲")
public enum CountryType {

    ASIA("AS","亚洲"),
    EUROPE("EU","欧洲"),
    NORTHAMERICAN("NA","北美洲"),
    SOUTHAMERICAN("SA","南美洲"),
    OCEANIN("OA","大洋洲"),
    AFRICAN("AF","非洲"),
    ANTARCTICA("AN","南极洲")
    ;

    private String code;

    private String message;

    CountryType(String code,String message){
        this.code = code;
        this.message = message;
    }

    private String getCode(){
        return code;
    }

    private String getMessage(){
        return message;
    }
}

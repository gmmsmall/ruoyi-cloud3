package com.ruoyi.acad.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 院士年龄信息
 * @Author guomiaomiao
 * @Date 2020/7/8 15:40
 * @Version 1.0
 */
@Data
@ApiModel("院士年龄信息")
public class AcadAgeInfo {

    @ApiModelProperty("学历基础信息")
    private List<AcadMileage> education;

    @ApiModelProperty("院士授衔年")
    private List<AcadMileage> aos;

    @ApiModelProperty("重大荣誉获得年")
    private List<AcadMileage> award;

}

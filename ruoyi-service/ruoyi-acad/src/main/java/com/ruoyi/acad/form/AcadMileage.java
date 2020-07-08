package com.ruoyi.acad.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 院士各时间段成就
 * @Author guomiaomiao
 * @Date 2020/7/8 15:37
 * @Version 1.0
 */
@Data
public class AcadMileage {

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("内容")
    private String content;
}

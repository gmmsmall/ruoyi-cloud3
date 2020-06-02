package com.ruoyi.acad.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description：基本信息表<br/>
 * CreateTime ：2020年3月11日上午10:03:34<br/>
 * CreateUser：ys<br/>
 */
@Data
@ApiModel(value = "展示数据类")
public class BaseInfoForm {

    @NotBlank(message = "{required}")
    @ApiModelProperty(value = "院士id", dataType = "Integer")
    private Integer acadId;

    @ApiModelProperty(value = "是否展示", dataType = "Boolean")
    private Boolean isShow;

    @ApiModelProperty(value = "是否拉黑", dataType = "Boolean")
    private Boolean isBlack;

}
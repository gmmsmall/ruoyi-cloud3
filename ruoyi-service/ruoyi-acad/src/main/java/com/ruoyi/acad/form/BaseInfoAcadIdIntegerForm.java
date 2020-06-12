package com.ruoyi.acad.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 院士编码表
 * @Author guomiaomiao
 * @Date 2020/6/12 9:38
 * @Version 1.0
 */
@Data
@ApiModel(value = "院士编码表(Integer类型)")
public class BaseInfoAcadIdIntegerForm {

    @ApiModelProperty(value = "院士id")
    @NotNull
    private Integer acadId;
}

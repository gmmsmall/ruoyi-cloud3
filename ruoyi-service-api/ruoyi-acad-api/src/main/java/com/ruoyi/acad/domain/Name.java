package com.ruoyi.acad.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description：院士姓名表<br/>
 * CreateTime ：2020年3月11日上午10:05:37<br/>
 * CreateUser：ys<br/>
 */
@Data
@ApiModel(value = "com.ruoyi.acad.domain.Name",description = "院士姓名信息表")
@AllArgsConstructor
public class Name implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    @ApiModelProperty("网站原始姓名")
    private String rawName;//网站原始姓名

    @ApiModelProperty("真实姓名")
    private String realName;//真实姓名

    @ApiModelProperty("英文名字")
    private String enName;//英文名字

    @ApiModelProperty("中文名字")
    private String cnName;//中文名字
}

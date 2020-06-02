package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士邮箱表<br/>
 * CreateTime ：2020年3月11日上午10:04:20<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_email")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("是否为主邮箱")
    private Boolean isMainEmail;

    @ApiModelProperty("是否为有效邮箱")
    private Boolean isEffectiveEmail;//是否为有效邮箱


}

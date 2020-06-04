package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士教育履历表<br/>
 * CreateTime ：2020年3月11日上午10:03:45<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_education")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    /**
     * 1-学士，2-硕士，3-博士，4-未知
     */
    @ApiModelProperty("1-学士，2-硕士，3-博士，4-未知")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer education;

    @ApiModelProperty("学校")
    private String school;//学校

    @ApiModelProperty("毕业时间")
    private Integer graduationYear;//毕业时间
}

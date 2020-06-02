package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士获奖情况表<br/>
 * CreateTime ：2020年3月11日上午10:02:55<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_award")
public class Award implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    @ApiModelProperty("项目名称")
    private String awardName;//项目名称

    @ApiModelProperty("奖项类别名称")
    private String awardCategory;//奖项类别名称

    @ApiModelProperty("获奖时间")
    private LocalDateTime awardYear;//获奖时间

    @ApiModelProperty("获奖介绍")
    private String awardProfile;//获奖介绍
}
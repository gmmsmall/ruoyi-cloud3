package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士科学院对应关系表<br/>
 * CreateTime ：2020年3月11日上午10:00:41<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_aos")
public class Aos implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    @ApiModelProperty("科学院")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long aosId;//科学院

    @ApiModelProperty("科学院名称")
    @NotNull
    private transient String aosName;//科学院名称

    @ApiModelProperty("当选年")
    private Integer electedYear;//当选年

    /**
     * 1-正籍，2-外籍，3-通讯，4-其他
     */
    @ApiModelProperty("1-正籍，2-外籍，3-通讯，4-其他")
    private Integer aosMemberType;

    @ApiModelProperty("科学院院士网页地址")
    private String acadWebsiteLink;//科学院院士网页地址

    @ApiModelProperty("科学院院士网页地址快照")
    private String acadWebsiteSnapshot;//科学院院士网页地址快照
}
package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士工作表<br/>
 * CreateTime ：2020年3月11日上午10:08:50<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_work")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码，必填")
    @NotNull
    private Integer acadId;

    @ApiModelProperty("工作单位(原文)")
    private String workUnit;//工作单位

    @ApiModelProperty("工作单位(翻译)")
    private String workUnitTrans;

    @ApiModelProperty("职务")
    private String jobTitle;//职务

    @ApiModelProperty("工作开始时间")
    private Integer jobStartYear;//工作开始时间

    @ApiModelProperty("工作结束时间")
    private Integer jobEndYear;//工作结束时间

    @ApiModelProperty("单位地址")
    private String workAddress;//单位地址
}

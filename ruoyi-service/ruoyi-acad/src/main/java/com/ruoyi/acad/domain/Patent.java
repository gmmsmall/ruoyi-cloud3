package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Description：院士专利表<br/>
 * CreateTime ：2020年3月11日上午10:07:41<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_patent")
public class Patent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private Integer acadId;

    @ApiModelProperty("专利id")
    private String patentId;

    @ApiModelProperty("专利名称")
    private String patentName;

    @ApiModelProperty("获得日期")
    private Date getTime;

    @ApiModelProperty("专利网站")
    private String patentWebsite;

}
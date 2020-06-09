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
 * Description：院士论文表<br/>
 * CreateTime ：2020年3月11日上午10:07:09<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_paper")
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private Integer acadId;

    @ApiModelProperty("论文ID")
    private String paperId;//论文ID

    @ApiModelProperty("论文题目")
    private String paperTitle;//论文题目

    @ApiModelProperty("论文摘要")
    private String paperAbstract;//论文摘要

    @ApiModelProperty("发表时间")
    private Date publishedTime;//发表时间

    @ApiModelProperty("论文发表刊物名称")
    private String paper_publication;//论文发表刊物名称

    @ApiModelProperty("引用数量")
    private Integer hIndex;//引用数量

    /**
     * 1-SCI，2-EI，3-IEEE，4-其他
     */
    @ApiModelProperty("论文类别 1-SCI，2-EI，3-IEEE，4-其他")
    private Integer periodical;//论文类别

    @ApiModelProperty("网页链接")
    private String paperWebsiteLink;//网页链接

}

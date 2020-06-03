package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description：院士联系信息表<br/>
 * CreateTime ：2020年3月11日上午10:08:28<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_sns")
public class Sns implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码，必填")
    @NotNull
    private Integer acadId;

    @ApiModelProperty("联系方式名称")
    private String snsRemark;//联系方式名称

    @ApiModelProperty("联系方式")
    private String snsValue;//联系方式

}

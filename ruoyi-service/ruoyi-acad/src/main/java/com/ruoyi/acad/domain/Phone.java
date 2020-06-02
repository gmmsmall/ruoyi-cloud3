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

/**
 * Description：院士电话表<br/>
 * CreateTime ：2020年3月11日上午10:08:02<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private Integer acadId;

    @ApiModelProperty("电话号码")
    private String phoneNumber;//电话号码

    /**
     * 1-手机，2-固化，3-传真，4-其他
     */
    @ApiModelProperty("电话类型，1-手机，2-固化，3-传真，4-其他")
    private Integer phoneType;

    /**
     * 1-本人，2-秘书，3-办公室，4-其他
     */
    @ApiModelProperty("电话所属人，1-本人，2-秘书，3-办公室，4-其他")
    private Integer phonePersonType;

    @ApiModelProperty("是否是主电话")
    private Boolean isMainPhone;//是否是主电话

    @ApiModelProperty("是否是有效电话")
    private Boolean isEffectivePhone;//是否是有效电话


}

package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 分组管理子表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.entity.SendMailGroupItems",description = "分组管理子表")
public class SendMailGroupItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id",hidden = true)
    private Long id;//主键

    /**
     * 分组主键
     */
    @ApiModelProperty("分组主键id，必填")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fid;

    /**
     * 院士编码
     */
    @ApiModelProperty("院士编码，必填")
    @NotEmpty
    private String acadencode;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除",hidden = true)
    private String deleteflag;

}

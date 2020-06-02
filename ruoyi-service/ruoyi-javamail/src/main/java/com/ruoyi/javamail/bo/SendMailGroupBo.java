package com.ruoyi.javamail.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * 分组管理操作实体类表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.entity.SendMailGroupBo",description = "分组管理操作实体类表")
public class SendMailGroupBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", dataType = "string")
    @NotEmpty
    private String name;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "新增人", hidden = true)
    private String addperson;

    @ApiModelProperty(value = "新增人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addpersonid;

}

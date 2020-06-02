package com.ruoyi.javamail.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分组管理详情展示表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.vo.SendMailGroupInfoVo",description = "分组管理详情展示表")
public class SendMailGroupInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", dataType = "string")
    private String name;

    /**
     * 分组子表，即每个分组下面包含多少院士
     */
    private List<SendMailGroupItems> itemsList;

}

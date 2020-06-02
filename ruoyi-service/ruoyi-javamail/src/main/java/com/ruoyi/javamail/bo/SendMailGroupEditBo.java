package com.ruoyi.javamail.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.javamail.entity.SendMailGroupItems;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * 分组管理修改实体类表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.entity.SendMailGroupEditBo",description = "分组管理修改实体类表")
public class SendMailGroupEditBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id，必填")
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
    @ApiModelProperty(value = "修改人", hidden = true)
    private String editperson;

    @ApiModelProperty(value = "修改人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long editpersonid;

    @ApiModelProperty("分组子表管理的院士编码集合")
    private List<SendMailGroupItems> itemsList;

}

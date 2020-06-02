package com.ruoyi.javamail.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 分组管理删除实体类表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.entity.SendMailGroupDeleteBo",description = "分组管理删除实体类表")
public class SendMailGroupDeleteBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id列表")
    private List<Long> idList;

    @ApiModelProperty(value = "修改人", hidden = true)
    private String editperson;

    @ApiModelProperty(value = "修改人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long editpersonid;

}

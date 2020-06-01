package com.ruoyi.javamail.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 模板管理删除实体类参数
 * @author gmm
 */
@Data
@ApiModel(value = "com.ruoyi.javamail.bo.TemplateManagerDeleteBo",description = "模板管理删除参数表")
public class TemplateManagerDeleteBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id列表")
    @NotEmpty
    private List<Long> idList;

    @ApiModelProperty(value = "新增人id",hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addpersonid;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "新增人姓名",hidden = true)
    private String addperson;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除",hidden = true)
    private String deleteflag;

}

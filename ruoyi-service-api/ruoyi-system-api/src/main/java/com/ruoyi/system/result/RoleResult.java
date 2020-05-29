package com.ruoyi.system.result;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("role")
public class RoleResult {
    /**
     * 角色ID
     */
    @Excel(name = "角色序号")
    @ApiModelProperty(value = "角色序号" , dataType = "string" , required = true)
    private Long roleId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @ApiModelProperty(value = "角色名称" , dataType = "string" , required = true)
    private String roleName;
}

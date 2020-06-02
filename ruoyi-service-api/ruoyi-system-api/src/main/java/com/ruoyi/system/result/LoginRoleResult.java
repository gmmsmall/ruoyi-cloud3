package com.ruoyi.system.result;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRoleResult {
    /**
     * 角色ID
     */
    @Excel(name = "角色序号")
    @ApiModelProperty(value = "角色序号", dataType = "integer", example = "123", hidden = true)
    private Long roleId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @ApiModelProperty(value = "角色名称", dataType = "string", required = true)
    private String roleName;

    /**
     * 角色描述
     */
    @Excel(name = "角色描述")
    @ApiModelProperty(value = "角色描述", dataType = "string", required = true)
    private String remark;
}

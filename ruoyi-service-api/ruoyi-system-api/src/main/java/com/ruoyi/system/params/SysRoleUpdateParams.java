package com.ruoyi.system.params;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysRoleUpdateParams {

    /**
     * 角色ID
     */
    @Excel(name = "角色序号")
    @ApiModelProperty(value = "角色序号", dataType = "integer", example = "123", required = true)
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

    @ApiModelProperty(value = "令牌编号，多个令牌编号以逗号分隔", dataType = "string", required = true)
    private String tokenNo;

    @ApiModelProperty(value = "科学院编号，多个科学院ID以逗号分隔", dataType = "string", required = true)
    private String aosNo;

}

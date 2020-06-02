package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@ApiModel(value = "user对象")
@Data
public class SysUserResult {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户序号", example = "123", required = true)
    private Long userId;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    @ApiModelProperty(value = "手机号码", required = true)
    private String phonenumber;

    @ApiModelProperty(value = "帐号状态 0=正常,1=停用", required = true)
    private String status;

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "创建时间", required = true)
    private String createTime;

    @ApiModelProperty(value = "角色ids", required = true)
    private Long[] roleIds;
}

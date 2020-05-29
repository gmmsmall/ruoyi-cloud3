package com.ruoyi.system.params;

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
public class UserParams {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    @ApiModelProperty(value = "手机号码", required = true)
    private String phonenumber;

    @ApiModelProperty(value = "帐号状态 0=正常,1=停用", required = true)
    private String status;

    @ApiModelProperty(value = "角色id", required = true)
    private Long roleId;

    @ApiModelProperty(value = "用户邮箱", required = true)
    private String email;

    @ApiModelProperty(value = "用户性别 0男 1女", required = true)
    private String sex;

    @ApiModelProperty(value = "用户头像", required = true)
    private String avatar;
}

package com.ruoyi.system.result;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.Token;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("登录出参")
public class LoginResult {
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "角色信息")
    private List<LoginRoleResult> sysRoleResults;
    @ApiModelProperty(value = "用户信息")
    private SysUserResult sysUserResult;
    @ApiModelProperty(value = "权限信息")
    private List<Token> tokenList;
    @ApiModelProperty(value = "科学院信息")
    private List<Aos> aosList;
//    private List<>
}

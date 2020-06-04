package com.ruoyi.system.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "修改密码")
@Data
public class ChangePwdParams {

    @ApiModelProperty(value = "原密码", required = true)
    private String oldPwd;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPwd;

    @ApiModelProperty(value = "确认密码", required = true)
    private String secPwd;

}

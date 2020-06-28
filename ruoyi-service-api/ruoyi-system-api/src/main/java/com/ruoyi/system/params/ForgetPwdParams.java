package com.ruoyi.system.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "忘记密码")
@Data
public class ForgetPwdParams {

    @ApiModelProperty(value = "手机号码", required = true)
    private String phonenumber;

    @ApiModelProperty(value = "密码", required = true)
    private String passwd;

    @ApiModelProperty(value = "校验id", required = true)
    private String checkStr;

}

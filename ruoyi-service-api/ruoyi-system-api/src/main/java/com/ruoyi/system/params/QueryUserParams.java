package com.ruoyi.system.params;

import com.ruoyi.system.result.QueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询用户model")
public class QueryUserParams extends QueryRequest {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户性别 0男 1女")
    private String sex;


}

package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TokenPerms对象", description = "")
public class TokenPermsResult {


    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @Size(max = 50, message = "{noMoreThan}")
    @ApiModelProperty(value = "权限", required = true)
    private String perms;

    @ApiModelProperty(value = "前端路由")
    private String route;

    @ApiModelProperty(value = "令牌编号")
    private String tokenNo;

    @ApiModelProperty(value = "父令牌编号")
    private String parentNo;
}

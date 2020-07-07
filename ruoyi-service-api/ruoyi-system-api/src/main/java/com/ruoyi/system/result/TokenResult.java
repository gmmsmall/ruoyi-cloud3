package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jxd
 */
@Data
@ApiModel(value = "Token")
public class TokenResult {

    @ApiModelProperty(value = "令牌编号")
    private String tokenNo;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

}

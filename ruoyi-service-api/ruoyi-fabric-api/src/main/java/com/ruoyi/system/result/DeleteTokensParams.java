package com.ruoyi.system.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteTokensParams {
    @NotBlank(message = "{required}")
    @ApiModelProperty(value = "令牌编号" , dataType = "string" , required = true)
    String tokenNos;
}

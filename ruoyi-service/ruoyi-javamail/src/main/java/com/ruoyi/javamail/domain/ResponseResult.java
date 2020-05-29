package com.ruoyi.javamail.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ResponseResult {

    @ApiModelProperty(value = "接口状态", example = "true|false", required = true)
    private Boolean status;
    @ApiModelProperty(value = "接口错误码", example = "200|500", required = true)
    private Integer errorCode;
    @ApiModelProperty(value = "接口描述", example = "成功|失败", required = true)
    private String errorDesc;
    @ApiModelProperty(value = "接口返回", example = "null|object", required = true)
    private Object object;


    public ResponseResult() {
    }

    public ResponseResult(Boolean status, Integer errorCode, String errorDesc, Object object) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.object = object;

    }
}

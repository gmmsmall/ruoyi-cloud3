package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
@ApiModel
public class RE<T> {

    @ApiModelProperty(value = "接口状态", example = "true|false", required = true)
    private Boolean status;
    @ApiModelProperty(value = "接口错误码", example = "200|500", required = true)
    private Integer errorCode;
    @ApiModelProperty(value = "接口描述", example = "成功|失败", required = true)
    private String errorDesc;
    @ApiModelProperty(value = "接口返回", example = "null|object", required = true)
    private T object;


    public RE() {
    }

    public RE(Boolean status, Integer errorCode, String errorDesc, T object) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.object = object;

    }

    public static RE ok() {
        return new RE(true, 200, "success", null);
    }

    public static RE error() {
        return new RE(false, 200, "fail", null);
    }

    public static RE ok(String msg) {
        return new RE(true, 200, msg, null);
    }

    public static RE ok(Object object) {
        return new RE(true, 200, "查询成功", object);
    }

    public static RE error(String msg) {
        return new RE(false, 200, msg, null);
    }

    public static RE error(Integer code, String msg) {
        return new RE(false, code, msg, null);
    }
}

package com.ruoyi.javamail.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果说明
 * @Author guomiaomiao
 * @Date 2020/5/29 16:50
 * @Version 1.0
 */
@ApiModel(value = "com.ruoyi.javamail.web.Rest",description = "返回结果说明")
public class Rest<T> {

    @ApiModelProperty(value = "是否成功")
    private boolean success=true;
    @ApiModelProperty(value = "返回对象")
    private T data;
    @ApiModelProperty(value = "错误编号")
    private Integer errCode;
    @ApiModelProperty(value = "错误信息")
    private String message;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public Integer getErrCode() {
        return errCode;
    }
    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

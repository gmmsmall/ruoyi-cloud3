package com.mingbyte.academician.bean.enums;

/**
 * 原则上，返回给前端的状态消息应该需要在这里预定义。
 */
public enum ResponseMsgEnum {
    SUCCESS("200", "成功"),
    UNKNOWN_ERROR("-100", "未知错误"),
    NOT_FOUND("404", "请求失败"),
    INTERNAL_SERVER_ERROR("500", "内部服务器错误"),
    PARAM_ERROR("9001", "参数不合法");

    private String code;
    private String msg;

    private ResponseMsgEnum(String code, String message) {
        this.code = code;
        this.msg = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}

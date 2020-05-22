package com.ruoyi.fdfs.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/28 0028.
 */
public class RespMsgBean<T> implements Serializable{

    private String code;
    private String msg;
    private T data;

    public RespMsgBean() {
        this.code = UpLoadConstant.kCode_Fail;
        this.msg = "";
    }



    public RespMsgBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RespMsgBean(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    //  默认方法

    public static RespMsgBean successMsg(String msg){
        RespMsgBean apiBean = new RespMsgBean(UpLoadConstant.kCode_Success, msg, null);
        return apiBean;
    }

    public static RespMsgBean success(Object data, String msg){
        RespMsgBean apiBean = new RespMsgBean(UpLoadConstant.kCode_Success, msg, data);
        return apiBean;
    }

    public static RespMsgBean success(Object data){
        RespMsgBean apiBean = new RespMsgBean(UpLoadConstant.kCode_Success, "", data);
        return apiBean;
    }

    public static RespMsgBean failure(){
        return failure("请求失败");
    }

    public static RespMsgBean failure(String msg){
        RespMsgBean apiBean = new RespMsgBean(UpLoadConstant.kCode_Fail, msg);
        return apiBean;
    }

    public static RespMsgBean failure(String code, String msg){
        RespMsgBean apiBean = new RespMsgBean(code, msg);
        return apiBean;
    }



    public static RespMsgBean sessionError(){
        RespMsgBean apiBean = new RespMsgBean(UpLoadConstant.kCode_SessionError, "登陆超时");
        return apiBean;
    }


}

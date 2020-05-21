package com.ruoyi.fabric.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mingbyte.academician.bean.enums.ResponseMsgEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * {@link ResponseResult}中使用的code和message需要在{@link ResponseMsgEnum}中预定义。
 * 没有数据返回时，默认返回空字符串“”。
 * 实体类与json互转的时候,属性值为null的不参与序列化.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -770255281125399160L;

    //返回数据
    private T data;
    //返回码
    private String code;
    //返回描述
    private String msg;

    public ResponseResult(){
        this(ResponseMsgEnum.SUCCESS);
    }

    // 没有返回数据时，默认返回空字符串“”。
    public ResponseResult(ResponseMsgEnum msgEnum){
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMessage();
        this.data = (T)"";
    }

    public ResponseResult(ResponseMsgEnum msgEnum, T data){
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMessage();
        this.data = data;
    }

    public ResponseResult(T data){
        this(ResponseMsgEnum.SUCCESS, data);
    }



    public String toString() {
        return " code=" + this.code + " message=" + this.msg + " data=" + this.data.toString();
    }


}

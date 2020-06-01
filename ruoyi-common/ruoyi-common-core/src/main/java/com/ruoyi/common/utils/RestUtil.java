package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.Rest;

/**
 * 返回数据类型工具类
 * @Author guomiaomiao
 * @Date 2020/6/1 11:50
 * @Version 1.0
 */

public class RestUtil {
    public static Rest success(Object object) {
        Rest result = new Rest();
        result.setErrCode(200);
        result.setMessage("操作成功");
        result.setData(object);
        return result;
    }

    public static Rest success() {
        return success(null);
    }

    public static Rest error(Integer code, String msg) {
        Rest result = new Rest();
        result.setErrCode(code);
        result.setMessage(msg);
        return result;
    }

}

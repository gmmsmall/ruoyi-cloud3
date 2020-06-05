package com.ruoyi.common.redis.util;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.system.domain.SysUser;

import javax.servlet.http.HttpServletRequest;

public class JWTUtil {

    public static SysUser getUser() {
        HttpServletRequest request = ServletUtils.getRequest();
        String token = request.getHeader("token");
        RedisUtils redisUtils = SpringContextUtil.getBean(RedisUtils.class);
        return redisUtils.get(Constants.ACCESS_TOKEN + token, SysUser.class);
    }

    public static String getToken() {
        HttpServletRequest request = ServletUtils.getRequest();
        return request.getHeader("token");
    }
}

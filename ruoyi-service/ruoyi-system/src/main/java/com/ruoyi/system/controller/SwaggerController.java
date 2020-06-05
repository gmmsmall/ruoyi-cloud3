package com.ruoyi.system.controller;

import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.system.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api("测试")
public class SwaggerController {


    @ApiOperation(value = "getUser ~", notes = "getUser")
    @GetMapping("/getUser")
    public SysUser getUser() {
        return JWTUtil.getUser();
    }

    @ApiOperation(value = "getToken ~", notes = "getToken")
    @GetMapping("/getToken")
    public String getToken() {
        return JWTUtil.getToken();
    }
}

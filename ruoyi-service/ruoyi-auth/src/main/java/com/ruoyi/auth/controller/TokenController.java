package com.ruoyi.auth.controller;

import com.ruoyi.auth.form.LoginForm;
import com.ruoyi.auth.service.AccessTokenService;
import com.ruoyi.auth.service.SysLoginService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.result.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api("登录")
public class TokenController {
    @Autowired
    private AccessTokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录")
    public LoginResult login(@RequestBody LoginForm form) {
        // 用户登录
        SysUser user = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return tokenService.createToken(user);
    }

    @PostMapping("logout")
    public R logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        SysUser user = tokenService.queryByToken(token);
        if (null != user) {
            sysLoginService.logout(user.getLoginName());
            tokenService.expireToken(user.getUserId());
        }
        return R.ok();
    }
}

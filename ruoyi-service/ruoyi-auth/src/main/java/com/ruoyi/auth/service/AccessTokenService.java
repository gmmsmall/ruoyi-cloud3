package com.ruoyi.auth.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.redis.annotation.RedisEvict;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.RemoteIBlockUserService;
import com.ruoyi.system.feign.RemoteRoleService;
import com.ruoyi.system.result.*;
import com.ruoyi.system.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("accessTokenService")
public class AccessTokenService {
    @Autowired
    private RedisUtils redis;
    @Autowired
    private RemoteIBlockUserService remoteIBlockUserService;
    @Autowired
    private RemoteRoleService remoteRoleService;

    /**
     * 12小时后过期
     */
    private final static long EXPIRE = 12 * 60 * 60;

    private final static String ACCESS_TOKEN = Constants.ACCESS_TOKEN;

    private final static String ACCESS_USERID = Constants.ACCESS_USERID;

    public SysUser queryByToken(String token) {
        return redis.get(ACCESS_TOKEN + token, SysUser.class);
    }

    @RedisEvict(key = "user_perms", fieldKey = "#sysUser.userId")
    public LoginResult createToken(SysUser sysUser) {
        // 生成token
        String token = IdUtil.fastSimpleUUID();
        // 保存或更新用户token
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("userId", sysUser.getUserId());
//        map.put("token", token);
//        map.put("expire", EXPIRE);
        // expireToken(userId);
        redis.set(ACCESS_TOKEN + token, sysUser, EXPIRE);
        redis.set(ACCESS_USERID + sysUser.getUserId(), token, EXPIRE);
        LoginResult loginResult = new LoginResult();
        //token
        loginResult.setToken(token);
        //角色信息
        String roleResult = remoteIBlockUserService.queryUserRole(String.valueOf(sysUser.getUserId()));
        if (roleResult != null) {
            FabricResult roleFabricResult = JSON.parseObject(roleResult, FabricResult.class);
            if (roleFabricResult.getCode() == FabricResult.RESULT_SUCC && roleFabricResult.getRoleList() != null) {
                List<LoginRoleResult> loginRoleResultList = new ArrayList<>();
                for (SysRoleResult sysRoleResult : roleFabricResult.getRoleList()) {
                    LoginRoleResult loginRoleResult = new LoginRoleResult();
                    BeanUtils.copyProperties(sysRoleResult, loginRoleResult);
                    loginRoleResultList.add(loginRoleResult);
                }
                loginResult.setSysRoleResults(loginRoleResultList);
            }
        }
        //用户信息
        SysUserResult sysUserResult = new SysUserResult();
        BeanUtils.copyProperties(sysUser, sysUserResult);
        List<String> strings = new ArrayList<>();
        for (SysRole s : sysUser.getRoles()) {
            strings.add(s.getRoleName());
        }
        sysUserResult.setRoleName(Joiner.on(",").join(strings));
        sysUserResult.setCreateTime(DateUtil.getDateFormat(sysUser.getCreateTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        loginResult.setSysUserResult(sysUserResult);
        //权限信息
        String tokenResult = remoteIBlockUserService.queryUserToken(String.valueOf(sysUser.getUserId()));
        if (tokenResult != null) {
            FabricResult tokenFabricResult = JSON.parseObject(tokenResult, FabricResult.class);
            if (tokenFabricResult.getCode() == FabricResult.RESULT_SUCC) {
                loginResult.setTokenList(tokenFabricResult.getTokenList());
            }
        }
        //科学院信息
        String aosResult = remoteIBlockUserService.queryUserAos(String.valueOf(sysUser.getUserId()));
        if (aosResult != null) {
            FabricResult aosFabricResult = JSON.parseObject(aosResult, FabricResult.class);
            if (aosFabricResult.getCode() == FabricResult.RESULT_SUCC) {
                loginResult.setAosList(aosFabricResult.getAosList());
            }
        }
        return loginResult;

    }

    public void expireToken(long userId) {
        String token = redis.get(ACCESS_USERID + userId);
        if (StringUtils.isNotBlank(token)) {
            redis.delete(ACCESS_USERID + userId);
            redis.delete(ACCESS_TOKEN + token);
        }
    }
}
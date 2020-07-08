package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.auth.annotation.HasPermissions;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.redis.util.JWTUtil;
import com.ruoyi.common.redis.util.RedisUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.RegexUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.params.*;
import com.ruoyi.system.result.*;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.util.IdGenerator;
import com.ruoyi.system.util.PasswordUtil;
import com.ruoyi.system.util.SendSms;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 用户 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("user")
@Api(value = "user", description = "用户管理")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("list")
    @HasPermissions("mbsystem:user:list")
    @ApiOperation(value = "查询用户列表", notes = "查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", dataType = "long", value = "页号", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", dataType = "long", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "userId", paramType = "query", dataType = "long", value = "用户ID"),
            @ApiImplicitParam(name = "userName", paramType = "query", dataType = "string", value = "用户名称"),
            @ApiImplicitParam(name = "phonenumber", paramType = "query", dataType = "string", value = "手机号码"),
            @ApiImplicitParam(name = "email", paramType = "query", dataType = "string", value = "用户邮箱"),
            @ApiImplicitParam(name = "sex", paramType = "query", dataType = "long", value = "用户性别 0男 1女"),
            @ApiImplicitParam(name = "roleName", paramType = "query", dataType = "string", value = "角色名称")
    })
    public ListResult<SysUserResult> list(QueryUserParams queryUserParams) {
        if (!StringUtil.isNullOrEmpty(queryUserParams.getPhonenumber())) {
            queryUserParams.setPhonenumber(RegexUtils.validateExprSpecialWord(queryUserParams.getPhonenumber()));
        }
        if (!StringUtil.isNullOrEmpty(queryUserParams.getRoleName())) {
            queryUserParams.setRoleName(RegexUtils.validateExprSpecialWord(queryUserParams.getRoleName()));
        }
        if (!StringUtil.isNullOrEmpty(queryUserParams.getUserName())) {
            queryUserParams.setUserName(RegexUtils.validateExprSpecialWord(queryUserParams.getUserName()));
        }
        return sysUserService.selectList(queryUserParams);
    }

    @ApiOperation(value = "用户路由权限", notes = "用户路由权限")
    @GetMapping("tokenList")
    public TokenTreeResult<TokenPermsResult> tokenList() {
        return sysUserService.tokenList();
    }

    /**
     * 新增保存用户
     */
    @PostMapping("save")
    @HasPermissions("mbsystem:user:affair")
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增保存用户", notes = "新增保存用户")
    public RE addSave(@RequestBody UserParams userParams) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(userParams.getUserName()))) {
            return RE.error("新增用户'" + userParams.getUserName() + "'失败，登录账号已存在");
        }
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(userParams))) {
            return RE.error("新增用户'" + userParams.getUserName() + "'失败，手机号已存在");
        }
        if (!RegexUtils.validateMobilePhone(userParams.getPhonenumber())) {
            return RE.error("新增用户手机号格式错误");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userParams, sysUser);
        sysUser.setLoginName(sysUser.getUserName());
        sysUser.setSalt(RandomUtil.randomStr(6));
        sysUser.setPassword(
                PasswordUtil.encryptPassword(sysUser.getLoginName(),
                        StringUtil.isNullOrEmpty(userParams.getPasswd()) ? Constants.DEFAULT_PASSWD : userParams.getPasswd(),
                        sysUser.getSalt()));
        sysUser.setCreateBy(getLoginName());
        List<Long> roleIds = new ArrayList<>();
        for (String l : userParams.getRoleIds().split(Constants.COMMA)) {
            roleIds.add(Long.valueOf(l));
        }
        sysUser.setRoleIds(roleIds);
        return sysUserService.insertUser(sysUser) > 0 ? RE.ok() : RE.error();
    }

    /**
     * 修改保存用户
     */
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    @HasPermissions("mbsystem:user:affair")
    @ApiOperation(value = "修改保存用户", notes = "修改保存用户")
    public RE editSave(@RequestBody UserUpdateParams userUpdateParams) {
        if (null != userUpdateParams.getUserId() && SysUser.isAdmin(userUpdateParams.getUserId())) {
            return RE.error("不允许修改超级管理员用户");
        }
        if (!RegexUtils.validateMobilePhone(userUpdateParams.getPhonenumber())) {
            return RE.error("修改用户手机号格式错误");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateParams, sysUser);
        sysUser.setUpdateBy(getLoginName());
        sysUser.setLoginName(sysUser.getUserName());
        List<Long> roleIds = new ArrayList<>();
        for (String l : userUpdateParams.getRoleIds().split(Constants.COMMA)) {
            roleIds.add(Long.valueOf(l));
        }
        sysUser.setRoleIds(roleIds);
        return sysUserService.updateUser(sysUser) > 0 ? RE.ok() : RE.error();
    }

    /**
     * 删除用户
     *
     * @throws Exception
     */
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    @HasPermissions("mbsystem:user:affair")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public RE remove(String ids) throws Exception {
        return sysUserService.deleteUserByIds(ids) > 0 ? RE.ok() : RE.error();
    }

    @GetMapping("getTokenPerms")
    @ApiOperation(value = "查询用户菜单权限", notes = "查询用户菜单权限")
    public RE perms(@RequestParam("userId") Long userId) {
        return RE.ok(sysUserService.selectPermsByUserId(userId));
    }

    /**
     * 查询用户
     */
    @GetMapping("detail")
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    public SysUserResult get() {
        return sysUserService.selectUserById(JWTUtil.getUser().getUserId());
    }

    @GetMapping("info")
    public SysUser info(@LoginUser SysUser sysUser) {
        List<PermsResult> permsResults = sysUserService.selectPermsByUserId(sysUser.getUserId());
        Set<String> strings = new HashSet<>();
        for (PermsResult p : permsResults) {
            strings.add(Arrays.toString(p.getPerm()));
        }
        sysUser.setButtons(strings);
        return sysUser;
    }

    /**
     * 查询用户
     */
    @GetMapping("/find/{username}")
    public RE findByUsername(@PathVariable("username") String username) {
        return RE.ok(sysUserService.selectUserByLoginName(username));
    }

    /**
     * 根据用户token获取科学院数据权限
     */
    @GetMapping("/getAosPerms")
    @ApiOperation(value = "根据用户token获取科学院数据权限", notes = "根据用户token获取科学院数据权限")
    @ApiImplicitParam(name = "token", paramType = "query", dataType = "string", value = "token")
    public RE getAosPerms(@RequestParam("token") String token) {
        return RE.ok(sysUserService.getAosPermsByToken(token));
    }

    //    @HasPermissions("system:user:changePwd")
    @OperLog(title = "修改密码", businessType = BusinessType.UPDATE)
    @PostMapping("/changePwd")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public RE changePwd(@RequestBody ChangePwdParams changePwdParams) {
        SysUser user = JWTUtil.getUser();
        if (user == null) {
            return RE.error("用户不存在");
        }
        String rediscode = redisUtils.get(Constants.PHONE_CODE_PREFIX + user.getPhonenumber());
        if (rediscode == null) {
            return RE.error("验证码已过期,请重新获取！");
        }
        if (!changePwdParams.getCode().equals(rediscode)) {
            return RE.error("验证码错误,请重新输入！");
        }
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), changePwdParams.getNewPwd(), user.getSalt()));
        return sysUserService.changeUserPwd(user) > 0 ? RE.ok() : RE.error();
    }

    //    @HasPermissions("system:user:resetPwd")
    @OperLog(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParam(name = "userId", paramType = "query", dataType = "Long", value = "用户id")
    public RE resetPwdSave(@RequestBody Long userId) {
        SysUser user = sysUserService.getUserById(userId);
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), Constants.DEFAULT_PASSWD, user.getSalt()));
        return sysUserService.changeUserPwd(user) > 0 ? RE.ok() : RE.error();
    }

    @GetMapping("/getCode")
    @ApiOperation(value = "获取手机验证码", notes = "获取手机验证码")
    @ApiImplicitParam(name = "phonenumber", paramType = "query", dataType = "string", value = "手机号")
    public RE getCode(String phonenumber) {
        try {
            if (StringUtils.isEmpty(phonenumber)) {
                SysUser user = JWTUtil.getUser();
                if (user == null) {
                    return RE.error("用户不存在");
                }
                Random random = new Random();
                int rannum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;
                String code = "{'code':" + rannum + "}";
                SendSms.SendSms(user.getPhonenumber(), code);
                redisUtils.set(Constants.PHONE_CODE_PREFIX + user.getPhonenumber(), String.valueOf(rannum), 60L);
                return new RE(true, 200, "发送短信验证码成功", user.getPhonenumber());
            } else {
                SysUser user = sysUserService.selectUserByPhoneNumber(phonenumber);
                if (user == null) {
                    return RE.error("用户不存在");
                }
                Random random = new Random();
                int rannum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;
                String code = "{'code':" + rannum + "}";
                SendSms.SendSms(phonenumber, code);
                redisUtils.set(Constants.PHONE_CODE_PREFIX + phonenumber, String.valueOf(rannum), 60L);
                return RE.ok("发送短信验证码成功");
            }
        } catch (Exception e) {
            return RE.error("发送短信验证码失败");
        }
    }

    @ApiOperation(value = "校验验证码", notes = "校验验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phonenumber", paramType = "query", dataType = "string", value = "手机号"),
            @ApiImplicitParam(name = "code", paramType = "query", dataType = "string", value = "验证码")
    })
    @GetMapping("/checkCode")
    public RE checkCode(@NotBlank(message = "{required}")
                                String code,
                        @NotBlank(message = "{required}")
                                String phonenumber) {
        String rediscode = redisUtils.get(Constants.PHONE_CODE_PREFIX + phonenumber);
        if (rediscode == null) {
            return RE.error("验证码已过期,请重新获取！");
        }
        if (!code.equals(rediscode)) {
            return RE.error("验证码错误,请重新输入！");
        }
        String str = IdGenerator.getId();
        redisUtils.set(Constants.FORGOT_PWD_PREFIX + phonenumber, str, 60L);
        return new RE(true, 200, "success", str);
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码")
    @PostMapping("/forgetPwd")
    public RE forgetPwd(@RequestBody ForgetPwdParams forgetPwdParams) {
        String str = redisUtils.get(Constants.FORGOT_PWD_PREFIX + forgetPwdParams.getPhonenumber());
        if (str == null) {
            return RE.error("操作超时，请重试！");
        }
        if (!str.equals(forgetPwdParams.getCheckStr())) {
            return RE.error("校验码错误，请重新输入！");
        }
        SysUser user = sysUserService.selectUserByPhoneNumber(forgetPwdParams.getPhonenumber());
        if (user == null) {
            return RE.error("用户不存在");
        }
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), forgetPwdParams.getPasswd(), user.getSalt()));
        return sysUserService.changeUserPwd(user) > 0 ? RE.ok() : RE.error();
    }
}

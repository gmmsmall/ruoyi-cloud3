package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.LoginUser;
import com.ruoyi.common.auth.annotation.HasPermissions;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.params.QueryUserParams;
import com.ruoyi.system.params.UserParams;
import com.ruoyi.system.params.UserUpdateParams;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.result.PermsResult;
import com.ruoyi.system.result.SysUserResult;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("user")
@Api("用户管理")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService sysUserService;

    @GetMapping("getUser")
    @ApiOperation(value = "获取当前用户", notes = "获取当前用户")
    public SysUser getUser() {
        return sysUserService.getUser();
    }

    @GetMapping("list")
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
        return sysUserService.selectList(queryUserParams);
    }


    /**
     * 新增保存用户
     */
//    @HasPermissions("system:user:add")
    @PostMapping("save")
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增保存用户", notes = "新增保存用户")
    public RE addSave(@RequestBody UserParams userParams) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(userParams.getUserName()))) {
            return RE.error("新增用户'" + userParams.getUserName() + "'失败，登录账号已存在");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userParams, sysUser);
        sysUser.setLoginName(sysUser.getUserName());
        sysUser.setSalt(RandomUtil.randomStr(6));
        sysUser.setPassword(
                PasswordUtil.encryptPassword(sysUser.getLoginName(), Constants.DEFAULT_PASSWD, sysUser.getSalt()));
        sysUser.setCreateBy(getLoginName());
//        List<String> roleNames = new ArrayList<>();
//        for (String l : userParams.getRoleIds().split(Constants.COMMA)) {
//            SysRole sysRole = sysRoleMapper.selectRoleById(Long.valueOf(l));
//            if (sysRole != null)
//                roleNames.add(sysRole.getRoleName());
//        }
//        sysUser.setRemark(Joiner.on(",").join(roleNames));
        return sysUserService.insertUser(sysUser) > 0 ? RE.ok() : RE.error();
    }

    /**
     * 修改保存用户
     */
//    @HasPermissions("system:user:edit")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    @ApiOperation(value = "修改保存用户", notes = "修改保存用户")
    public RE editSave(@RequestBody UserUpdateParams userUpdateParams) {
        if (null != userUpdateParams.getUserId() && SysUser.isAdmin(userUpdateParams.getUserId())) {
            return RE.error("不允许修改超级管理员用户");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateParams, sysUser);
        sysUser.setUpdateBy(getLoginName());
        sysUser.setLoginName(sysUser.getUserName());
//        List<String> roleNames = new ArrayList<>();
//        for (String l : userUpdateParams.getRoleIds().split(Constants.COMMA)) {
//            SysRole sysRole = sysRoleMapper.selectRoleById(Long.valueOf(l));
//            if (sysRole != null)
//                roleNames.add(sysRole.getRoleName());
//        }
//        sysUser.setRemark(Joiner.on(",").join(roleNames));
        return sysUserService.updateUser(sysUser) > 0 ? RE.ok() : RE.error();
    }

    /**
     * 删除用户
     *
     * @throws Exception
     */
    @HasPermissions("system:user:remove")
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public RE remove(String ids) throws Exception {
        return sysUserService.deleteUserByIds(ids) > 0 ? RE.ok() : RE.error();
    }

    @GetMapping("perms")
    @ApiOperation(value = "查询用户菜单权限", notes = "查询用户菜单权限")
    @ApiImplicitParam(name = "userId", paramType = "query", dataType = "long", value = "用户ID", required = true)
    public List<PermsResult> perms(Long userId) {
        return sysUserService.selectPermsByUserId(userId);
    }

    /**
     * 查询用户
     */
    @GetMapping("get")
    @OperLog(title = "查询用户", businessType = BusinessType.INSERT)
    @ApiOperation(value = "查询用户", notes = "查询用户")
    @ApiImplicitParam(name = "userId", paramType = "query", dataType = "long", value = "用户ID")
    public SysUserResult get(Long userId) {
        return sysUserService.selectUserById(userId);
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
    public SysUser findByUsername(@PathVariable("username") String username) {
        return sysUserService.selectUserByLoginName(username);
    }

//    /**
//     * 修改用户信息
//     *
//     * @param sysUser
//     * @return
//     * @author zmr
//     */
//    @HasPermissions("system:user:edit")
//    @PostMapping("update/info")
//    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
//    public R updateInfo(@RequestBody SysUser sysUser) {
//        return toAjax(sysUserService.updateUserInfo(sysUser));
//    }
//
//    /**
//     * 记录登陆信息
//     *
//     * @param sysUser
//     * @return
//     * @author zmr
//     */
//    @PostMapping("update/login")
//    public R updateLoginRecord(@RequestBody SysUser sysUser) {
//        return toAjax(sysUserService.updateUser(sysUser));
//    }
//
//    @HasPermissions("system:user:resetPwd")
//    @OperLog(title = "重置密码", businessType = BusinessType.UPDATE)
//    @PostMapping("/resetPwd")
//    public R resetPwdSave(@RequestBody SysUser user) {
//        user.setSalt(RandomUtil.randomStr(6));
//        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
//        return toAjax(sysUserService.resetUserPwd(user));
//    }
//
//    /**
//     * 修改状态
//     *
//     * @param sysUser
//     * @return
//     * @author zmr
//     */
//    @HasPermissions("system:user:edit")
//    @PostMapping("status")
//    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
//    public R status(@RequestBody SysUser sysUser) {
//        return toAjax(sysUserService.changeStatus(sysUser));
//    }
}

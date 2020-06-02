package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.system.domain.RoleForQuery;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.params.SysRoleParams;
import com.ruoyi.system.params.SysRoleUpdateParams;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.result.RoleResult;
import com.ruoyi.system.result.SysRoleResult;
import com.ruoyi.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色 提供者
 *
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("role")
@Api("角色管理")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 查询角色
     */
    @GetMapping("get")
    @ApiOperation(value = "查询角色的权限", notes = "查询角色的权限")
    @ApiImplicitParam(name = "roleId", paramType = "query", dataType = "long", value = "角色ID", required = true)
    public RE get(Long roleId) {
        try {
            return RE.ok(sysRoleService.selectRoleById(roleId));
        } catch (Exception e) {
            return RE.error();
        }
    }

    /**
     * 查询角色列表
     */
    @GetMapping("list")
    @ApiOperation(value = "查询角色列表", notes = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", dataType = "long", value = "页号", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", dataType = "long", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "roleName", paramType = "query", dataType = "string", value = "角色名", required = false),
            @ApiImplicitParam(name = "remark", paramType = "query", dataType = "string", value = "角色描述", required = false)
    })
    public RE list(RoleForQuery roleForQuery) {
        try {
            return RE.ok(sysRoleService.selectRoleList(roleForQuery));
        } catch (Exception e) {
            return RE.error();
        }
    }

    @GetMapping("roleList")
    @ApiOperation(value = "查询角色列表", notes = "查询角色列表----用于用户新建、编辑选择角色")
    public RE roleList() {
        try {
            RoleForQuery roleForQuery = new RoleForQuery();
            roleForQuery.setPageSize(9999999);
            roleForQuery.setPageNum(1);
            ListResult<SysRoleResult> roleListResult = sysRoleService.selectRoleList(roleForQuery);
            List<SysRoleResult> rolelist = new ArrayList<>();
            if (roleListResult != null) {
                rolelist = roleListResult.getRows();
            }
            List<RoleResult> roleResultList = new ArrayList<>();
            for (SysRoleResult sysRoleResult : rolelist) {
                RoleResult roleResult = new RoleResult();
                BeanUtils.copyProperties(sysRoleResult, roleResult);
                roleResultList.add(roleResult);
            }
            return RE.ok(roleResultList);
        } catch (Exception e) {
            return RE.error();
        }
    }

    /**
     * 新增保存角色
     */
    @PostMapping("save")
    @OperLog(title = "角色管理", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增保存角色", notes = "新增保存角色")
    public RE addSave(@RequestBody SysRoleParams role) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
//        sysRole.setRoleKey(sysRole.getRoleName());
//        sysRole.setStatus("0");
//        sysRole.setRoleSort("0");
        return sysRoleService.insertRole(sysRole) > 0 ? RE.ok() : RE.error();
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    @ApiOperation(value = "修改保存角色", notes = "修改保存角色")
    public RE editSave(@RequestBody SysRoleUpdateParams role) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
//        sysRole.setRoleKey(sysRole.getRoleName());
//        sysRole.setStatus("0");
//        sysRole.setRoleSort("0");
        return sysRoleService.updateRole(sysRole) > 0 ? RE.ok() : RE.error();
    }

//    /**
//     * 修改保存角色
//     */
//    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
//    @PostMapping("status")
//    public RE status(@RequestBody SysRole role) {
//        return sysRoleService.changeStatus(role) > 0 ? new RE().ok() : new RE().error();
//    }

    /**
     * 删除角色
     *
     * @throws Exception
     */
    @OperLog(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "ids", paramType = "query", dataType = "string", value = "角色ID", required = true)
    public RE remove(String ids) throws Exception {
        return sysRoleService.deleteRoleByIds(ids) > 0 ? RE.ok() : RE.error();
    }
}

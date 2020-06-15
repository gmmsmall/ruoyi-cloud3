package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.RuoyiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.feign.RemoteIBlockRoleService;
import com.ruoyi.system.result.FabricResult;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.result.PermResult;
import com.ruoyi.system.result.SysRoleResult;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private RemoteIBlockRoleService remoteIBlockRoleService;

    /**
     * 根据条件分页查询角色数据
     *
     * @param
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public ListResult<SysRoleResult> selectRoleList(RoleForQuery roleForQuery) {
        String result = remoteIBlockRoleService.queryRoles(roleForQuery);
        if (result != null) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC && fabricResult.getRoleList() != null) {
                List<SysRoleResult> sysRoleResults = fabricResult.getRoleList();
                sysRoleResults.sort((o1, o2) -> (o2.getRoleId() == null || o1.getRoleId() == null) ? 0 : o2.getRoleId().compareTo(o1.getRoleId()));
                return ListResult.list(sysRoleResults, (long) fabricResult.getTotal(), roleForQuery);
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        return null;
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public PermResult selectRoleById(Long roleId) {
        PermResult permResult = new PermResult();
        String result = remoteIBlockRoleService.queryRolePerms(String.valueOf(roleId));
        if (result != null) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
                permResult.setTokenList(fabricResult.getTokenList());
                permResult.setAosList(fabricResult.getAosList());
                permResult.setRoleName(fabricResult.getRoleName());
            }
        } else {
            throw new RuoyiException(Constants.CHANAL_CONNECTED_FAILED, 500);
        }
        return permResult;
    }


    /**
     * 批量删除角色信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deleteRoleByIds(String ids) throws BusinessException {
        Long[] roleIds = Convert.toLongArray(ids);
        if (roleIds.length > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("roleIds", roleIds);
            String result = remoteIBlockRoleService.deleteRole(params);
            return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? 1 : 0;
        }
        return 0;
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        return updateRoleMenu(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        if (StringUtils.isNotBlank(role.getTokenNo())) {
            String[] tokenNos = role.getTokenNo().split(Constants.COMMA);
//            setRoleToken(role, tokenNos);
            rows += tokenNos.length;
            role.setTokenNos(tokenNos);
        }
        if (StringUtils.isNotBlank(role.getAosNo())) {
            String[] aosNos = role.getAosNo().split(Constants.COMMA);
//            setRoleAos(role, aosNos);
            rows += aosNos.length;
            role.setAosNos(aosNos);
        }
        SysRoleResult sysRoleResult = new SysRoleResult();
        BeanUtils.copyProperties(role, sysRoleResult);
        sysRoleResult.setRoleId(System.currentTimeMillis());
        sysRoleResult.setCreateTime(LocalDateTime.now());
        String result = remoteIBlockRoleService.addRole(sysRoleResult);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? rows : 0;
    }

    /**
     * 修改角色菜单信息
     *
     * @param role 角色对象
     */
    public int updateRoleMenu(SysRole role) {
        int rows = 1;
        if (StringUtils.isNotBlank(role.getTokenNo())) {
            String[] tokenNos = role.getTokenNo().split(Constants.COMMA);
//            setRoleToken(role, tokenNos);
            rows += tokenNos.length;
            role.setTokenNos(tokenNos);
        }
        if (StringUtils.isNotBlank(role.getAosNo())) {
            String[] aosNos = role.getAosNo().split(Constants.COMMA);
//            setRoleAos(role, aosNos);
            rows += aosNos.length;
            role.setAosNos(aosNos);
        }
        SysRoleResult sysRoleResult = new SysRoleResult();
        BeanUtils.copyProperties(role, sysRoleResult);
        sysRoleResult.setUpdateTime(LocalDateTime.now());
        String result = remoteIBlockRoleService.updateRole(sysRoleResult);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? rows : 0;
    }
}

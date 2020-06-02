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
import com.ruoyi.system.mapper.RoleAosMapper;
import com.ruoyi.system.mapper.RoleTokenMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.result.FabricResult;
import com.ruoyi.system.result.ListResult;
import com.ruoyi.system.result.PermResult;
import com.ruoyi.system.result.SysRoleResult;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private RemoteIBlockRoleService remoteIBlockRoleService;

    @Autowired
    private RoleTokenMapper roleTokenMapper;

    @Autowired
    private RoleAosMapper roleAosMapper;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public ListResult<SysRoleResult> selectRoleList(RoleForQuery roleForQuery) {
        String result = remoteIBlockRoleService.queryRoles(roleForQuery);
        if (result != null) {
            FabricResult fabricResult = JSON.parseObject(result, FabricResult.class);
            if (fabricResult.getCode() == FabricResult.RESULT_SUCC) {
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
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Long userId) {
        List<SysRole> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
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
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public boolean deleteRoleById(Long roleId) {
        return roleMapper.deleteRoleById(roleId) > 0 ? true : false;
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
//        for (Long roleId : roleIds) {
//            SysRole role = roleMapper.selectRoleById(roleId);
//            if (countUserRoleByRoleId(roleId) > 0) {
//                throw new BusinessException(String.format("%1$s已分配,不能删除", role.getRoleName()));
//            }
//        }
        if (roleIds.length > 0) {
//            roleMapper.deleteRoleByIds(roleIds);
//            roleAosMapper.deleteByIds(roleIds);
//            roleTokenMapper.deleteByIds(roleIds);
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
        // 新增角色信息
//        roleMapper.insertRole(role);
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
        // 修改角色信息
//        roleMapper.updateRole(role);
//
//        roleAosMapper.deleteByIds(new Long[]{role.getRoleId()});
//        roleTokenMapper.deleteByIds(new Long[]{role.getRoleId()});
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
        String result = remoteIBlockRoleService.updateRole(sysRoleResult);
        return result != null && JSON.parseObject(result, FabricResult.class).getCode() == FabricResult.RESULT_SUCC ? rows : 0;
    }

    private void setRoleToken(SysRole role, String[] tokenNos) {
        Arrays.stream(tokenNos).forEach(tokenNo -> {
            RoleToken rt = new RoleToken();
            rt.setRoleId(role.getRoleId());
            rt.setTokenNo(tokenNo);
            roleTokenMapper.insertRoleToken(rt);
        });
    }

    private void setRoleAos(SysRole role, String[] aosNos) {
        Arrays.stream(aosNos).forEach(aosNo -> {
            RoleAos ra = new RoleAos();
            ra.setAosNo(aosNo);
            ra.setRoleId(role.getRoleId());
            roleAosMapper.insertRoleAos(ra);
        });
    }


    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 角色状态修改
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, String userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long roleId, String userIds) {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : users) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}

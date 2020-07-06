package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.RE;
import com.ruoyi.system.domain.RoleForQuery;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.result.*;

import java.util.Set;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
public interface ISysRoleService {
    /**
     * 根据条件分页查询角色数据
     *
     * @return 角色数据集合信息
     */
    public ListResult<SysRoleListResult> selectRoleList(RoleForQuery roleForQuery);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public PermResult selectRoleById(Long roleId);

    /**
     * 批量删除角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public RE deleteRoleByIds(String ids) throws Exception;

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

}

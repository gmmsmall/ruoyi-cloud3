package com.ruoyi.fabric.service;

import com.ruoyi.fabric.bean.Role;
import com.ruoyi.fabric.utils.Page;

import java.util.Map;

public interface IBlockRole {

    /**
     * 新增角色权限
     * @param role
     * @return 新增是否成功
     */
    public String add(Role role);


    /**
     * 更新角色权限
     * @param role
     * @return
     */
    public String update(Role role);


    /**
     * 遍历角色列表
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param remark
     * @return Page
     */
    public Page query(int pageNum , int pageSize, String roleName, String remark );

    /**
     *
     * @param roleIds
     * @return
     */
    public int delete(String roleIds);

    /**
     *
     * @param roleId
     * @return
     */
    public Map queryRolePerms(String roleId);

}

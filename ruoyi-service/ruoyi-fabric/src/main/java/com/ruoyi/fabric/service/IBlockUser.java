package com.ruoyi.fabric.service;


import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.Token;

import java.util.List;

public interface IBlockUser {

    /**
     * 新增用户角色关系
     *
     * @param roleIds
     * @param roleIds
     * @return
     */
    public String uploadUserRole(String userId, String roleIds);

    /**
     * 遍历用户角色列表
     *
     * @param userId
     * @return
     */
    public List<SysRole> queryUserRole(String userId);

    /**
     * 遍历用户令牌列表
     *
     * @param userId
     * @return
     */
    public List<Token> queryUserToken(String userId);

    /**
     * 遍历用户科学院列表
     *
     * @param userId
     * @return
     */
    public List<Aos> queryUserAos(String userId);


}

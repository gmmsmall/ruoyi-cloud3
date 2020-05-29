package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.RoleToken;

/**
 * @author jxd
 */
public interface RoleTokenMapper {

    void insertRoleToken(RoleToken roleToken);

    public int deleteByIds(Long[] ids);
}

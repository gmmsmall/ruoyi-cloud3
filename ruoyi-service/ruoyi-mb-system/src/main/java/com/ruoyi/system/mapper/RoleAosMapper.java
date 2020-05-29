package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.RoleAos;

/**
 * @author jxd
 */
public interface RoleAosMapper {

    void insertRoleAos(RoleAos roleAos);

    public int deleteByIds(Long[] ids);
}

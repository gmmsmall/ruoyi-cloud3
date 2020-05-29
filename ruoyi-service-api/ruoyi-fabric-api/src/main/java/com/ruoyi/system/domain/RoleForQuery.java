package com.ruoyi.system.domain;

import com.ruoyi.system.result.QueryRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息
 *
 * @author xwp
 */
@Getter
@Setter
public class RoleForQuery extends QueryRequest {
    private String roleName;     //角色名
    private String remark;
}

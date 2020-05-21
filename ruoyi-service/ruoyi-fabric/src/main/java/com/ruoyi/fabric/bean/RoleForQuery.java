package com.ruoyi.fabric.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息
 * @author xwp
 */
@Getter
@Setter
public class RoleForQuery {
    private long  pageNum;		 //角色ID
    private long  pageSize;
    private String roleName;     //角色名
}

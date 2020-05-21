package com.ruoyi.system.domain;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息
 * @author xwp
 */
@Getter
@Setter
public class Role {
    private long  roleId;		 //角色ID
    private String roleName;     //角色名
    private String  remark;	     //角色描述
    private JSONArray tokenNos;			//令牌编号
    private JSONArray  aosNos;			   //科学院编号
}

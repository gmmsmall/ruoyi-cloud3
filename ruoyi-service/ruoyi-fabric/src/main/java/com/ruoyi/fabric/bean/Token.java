package com.ruoyi.fabric.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 令牌信息
 * @author xwp
 */
@Getter
@Setter
public class Token {
    private String tokenNo;		 //令牌信息编号
    private String parentNo;	 //上一级令牌编号
    private String name;		 //名称
    private String perms;		 //权限标志，例如：user:add
    private int type;		     //类型， 1-菜单，2-按钮，3-自定义标签
    private long orderNum;		 //序号
}

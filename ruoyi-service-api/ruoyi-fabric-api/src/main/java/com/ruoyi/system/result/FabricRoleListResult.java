package com.ruoyi.system.result;

import lombok.Data;

import java.util.List;

@Data
public class FabricRoleListResult {

    private int code;

    private int pageSize;

    private int pageNum;

    private int total;

    private int size;

    private List<SysRoleListResult> roleList;

}

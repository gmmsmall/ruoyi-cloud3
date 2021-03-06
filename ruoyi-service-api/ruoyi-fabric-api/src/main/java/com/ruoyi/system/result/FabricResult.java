package com.ruoyi.system.result;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.Token;
import lombok.Data;

import java.util.List;

@Data
public class FabricResult {

    public static final int RESULT_SUCC = 0;

    public static final int RESULT_FAIL = 1;

    private int code;

    private int pageSize;

    private int pageNum;

    private int total;

    private int size;

    private String roleName;

    private List<SysRoleResult> roleList;

    private List<Token> tokenList;

    private List<Aos> aosList;

    private List<String> userIds;
}

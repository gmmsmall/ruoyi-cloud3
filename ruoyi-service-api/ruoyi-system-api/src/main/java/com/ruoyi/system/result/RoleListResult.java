package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("分页实体类")
public class RoleListResult {
    @ApiModelProperty(value = "当前页数", example = "123", required = true)
    public Integer pageNum;
    @ApiModelProperty(value = "总页数", example = "123", required = true)
    public Long total;
    @ApiModelProperty(value = "数据", required = true)
    public List<SysRoleResult> rows;

    public static RoleListResult list(List<SysRoleResult> list, Long total, QueryRequest queryRequest) {
        RoleListResult listResult = new RoleListResult();
        listResult.setPageNum(queryRequest.getPageNum());
        if (total <= queryRequest.getPageSize()) {
            listResult.setTotal(1L);
        } else {
            if (total % queryRequest.getPageSize() == 0) {
                listResult.setTotal(total / queryRequest.getPageSize());
            } else {
                listResult.setTotal((total / queryRequest.getPageSize()) + 1);
            }
        }
        listResult.setRows(list);
        return listResult;
    }
}

package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(value = "分页", description = "")
@NoArgsConstructor
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    @ApiModelProperty(value = "每页行数", dataType = "int", example = "123")
    private int pageSize = 10;
    @ApiModelProperty(value = "页号", dataType = "int", example = "123")
    private int pageNum = 1;

    @ApiModelProperty(hidden = true)
    private String sortField;
    @ApiModelProperty(hidden = true)
    private String sortOrder;

    public QueryRequest(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}

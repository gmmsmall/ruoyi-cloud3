package com.ruoyi.javamail.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "分页", description = "")
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    @ApiModelProperty(value = "每页行数", dataType = "int")
    private int pageSize = 10;
    @ApiModelProperty(value = "页号", dataType = "int")
    private int pageNum = 1;

    @ApiModelProperty(hidden = true)
    private String sortField;
    @ApiModelProperty(hidden = true)
    private String sortOrder;
}

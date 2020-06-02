package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("token查询")
public class TokenForQuery {

    @ApiModelProperty(value = "页号" , dataType = "string" , hidden = true)
    private long pageNum;
    @ApiModelProperty(value = "每页行数" , dataType = "string" , hidden = true)
    private long pageSize;
    @ApiModelProperty(value = "令牌编号" , dataType = "string")
    private String tokenNo;
    @ApiModelProperty(value = "名称" , dataType = "string")
    private String name;
    @ApiModelProperty(value = "权限标志" , dataType = "string")
    private String perms;
    @ApiModelProperty(value = "类型， 1-菜单，2-按钮，3-自定义标签" , dataType = "string")
    private Integer type;
}

package com.ruoyi.system.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("令牌树")
public class TokenTreeResult<T> {
    @ApiModelProperty(value = "令牌编号")
    private String tokenNo;
    @ApiModelProperty(value = "父令牌编号")
    private String parentNo;
    @ApiModelProperty(value = "令牌名称")
    private String name;
    @ApiModelProperty(value = "权限标志")
    private String perms;
    @ApiModelProperty(value = "子菜单")
    private List<TokenTreeResult<T>> children;
    @ApiModelProperty(value = "有无父菜单")
    private boolean hasParent = false;
    @ApiModelProperty(value = "有无子菜单")
    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}

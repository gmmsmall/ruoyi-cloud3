package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("令牌树")
public class TokenTree<T> {
    @ApiModelProperty(value = "令牌id",hidden = true)
    private Long tokenId;
    @ApiModelProperty(value = "令牌编号")
    private String tokenNo;
    @ApiModelProperty(value = "父令牌编号")
    private String parentNo;
    @ApiModelProperty(value = "令牌名称")
    private String name;
    @ApiModelProperty(value = "权限标志")
    private String perms;
    @ApiModelProperty(value = "类型，1-菜单，2-按钮，3-自定义标签")
    private Integer tokenType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "子菜单")
    private List<TokenTree<T>> children;
    @ApiModelProperty(value = "有无父菜单")
    private boolean hasParent = false;
    @ApiModelProperty(value = "有无子菜单")
    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}

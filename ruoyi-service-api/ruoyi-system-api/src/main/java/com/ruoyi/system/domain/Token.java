package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Token对象", description = "")
public class Token {

    private static final long serialVersionUID = 1L;
    public static final int TYPE_MENU = 1;
    public static final int TYPE_BUTTON = 2;
    public static final int TYPE_LABEL = 3;

    //当前用户是否拥有权限
    public static final int IS_CHECK = 1;
    public static final int NO_CHECK = 0;

//    @ApiModelProperty(value = "令牌ID", hidden = true)
//    private Long tokenId;

    @ApiModelProperty(value = "令牌编号")
    private String tokenNo;

    @ApiModelProperty(value = "上一级令牌编号")
    private String parentNo;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @Size(max = 50, message = "{noMoreThan}")
    @ApiModelProperty(value = "权限", required = true)
    private String perms;

    @ApiModelProperty(value = "路由", required = true)
    private String route;

    @NotNull(message = "{required}")
    @ApiModelProperty(value = "类型，1-菜单，2-按钮，3-自定义标签", required = true)
    private Integer type;

    @ApiModelProperty(value = "序号", hidden = true)
    private Integer orderNum = 0;

    @ApiModelProperty(value = "是否有权限 0否 1是", hidden = true)
    private Integer isCheck = 0;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

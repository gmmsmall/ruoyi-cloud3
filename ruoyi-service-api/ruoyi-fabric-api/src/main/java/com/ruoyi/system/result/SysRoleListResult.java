package com.ruoyi.system.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.Token;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysRoleListResult {
    /**
     * 角色ID
     */
    @Excel(name = "角色序号")
    @ApiModelProperty(value = "角色序号" , dataType = "string" , hidden = true)
    private Long roleId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @ApiModelProperty(value = "角色名称", dataType = "string", required = true)
    private String roleName;

    /**
     * 角色描述
     */
    @Excel(name = "角色描述")
    @ApiModelProperty(value = "角色描述", dataType = "string", required = true)
    private String remark;

    @ApiModelProperty(value = "令牌编号，多个令牌编号以逗号分隔", dataType = "string", required = true)
    private List<TokenListResult> tokens;

    @ApiModelProperty(value = "科学院编号，多个科学院ID以逗号分隔", dataType = "string", required = true)
    private List<AosListResult> aoss;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

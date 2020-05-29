package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.parser.Token;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "role对象")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Excel(name = "角色序号")
    private Long roleId;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @ApiModelProperty(value = "角色名称" , dataType = "string" , required = true)
    private String roleName;

    /**
     * 角色权限
     */
    @Excel(name = "角色权限")
    @ApiModelProperty(value = "角色权限" , dataType = "string" , required = true)
    private String roleKey;

    /**
     * 角色排序
     */
    @Excel(name = "角色排序")
    @ApiModelProperty(value = "角色排序" , dataType = "string" , required = true)
    private String roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定数据权限）
     */
    @Excel(name = "数据范围" , readConverterExp = "1=所有数据权限,2=自定义数据权限")
    @ApiModelProperty(hidden = true)
    private String dataScope;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态 0=正常,1=停用" , dataType = "String" , required = true)
    @Excel(name = "角色状态" , readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    @ApiModelProperty(hidden = true)
    private List<Long> menuIds;

    /**
     * 部门组（数据权限）
     */
    @ApiModelProperty(hidden = true)
    private Long[] deptIds;

    @ApiModelProperty(hidden = true)
    private String[] tokenNos;
    @ApiModelProperty(hidden = true)
    private String[] aosNos;
    @ApiModelProperty(hidden = true)
    private List<Token> tokenList;
    @ApiModelProperty(hidden = true)
    private List<Aos> aosList;

    @ApiModelProperty(value = "令牌编号，多个令牌编号以逗号分隔" , dataType = "string" , required = true)
    private transient String tokenNo;

    @ApiModelProperty(value = "科学院编号，多个科学院ID以逗号分隔" , dataType = "string" , required = true)
    private transient String aosNo;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId" , getRoleId())
                .append("roleName" , getRoleName())
                .append("roleKey" , getRoleKey())
                .append("roleSort" , getRoleSort())
                .append("dataScope" , getDataScope())
                .append("status" , getStatus())
                .append("delFlag" , getDelFlag())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateBy" , getUpdateBy())
                .append("updateTime" , getUpdateTime())
                .append("remark" , getRemark())
                .toString();
    }

}

package com.ruoyi.javamail.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
@Excel("用户信息表")
@ApiModel(value = "用户信息", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";

    // 默认密码
    public static final String DEFAULT_PASSWORD = "123456";

    @TableId(value = "USER_ID", type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

    @Size(min = 4, max = 20, message = "{range}")
    @ExcelField(value = "用户名")
    @ApiModelProperty(value = "用户名，4-20个字符", required = true, dataType = "string")
    @NotBlank(message = "{required}")
    private String username;

    @ApiModelProperty(value = "密码", dataType = "string")
    private String password;

    @ApiModelProperty(value = "部门ID", hidden = true)
    private Long deptId;

    @ExcelField(value = "部门")
    @ApiModelProperty(value = "部门名称", hidden = true)
    private transient String deptName;

    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    @ApiModelProperty(value = "邮箱", hidden = true)
    private String email;

   // @Pattern(regexp = RegexpConstant.MOBILE_REG, message = "{mobile}")
    @ExcelField(value = "手机号")
    @ApiModelProperty(value = "手机号", dataType = "string")
    private String mobile;

    @NotBlank(message = "{required}")
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    @ApiModelProperty(value = "状态，0=锁定,1=有效", dataType = "int", required = true)
    @NotBlank(message = "{required}")
    private String status;

    //@ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date modifyTime;

    //@ExcelField(value = "最后登录时间", writeConverter = TimeConverter.class)
    @ApiModelProperty(value = "最后登录时间", hidden = true)
    private Date lastLoginTime;

    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    @ApiModelProperty(value = "性别，0=男,1=女,2=保密", dataType = "int", hidden = true)
    private String ssex;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    @ApiModelProperty(hidden = true)
    private String description;

    @ApiModelProperty(value = "头像", hidden = true)
    private String avatar;

    @NotBlank(message = "{required}")
    @ApiModelProperty(value = "角色ID，多个角色ID使用逗号分隔", dataType = "int", required = true)
    @NotBlank(message = "{required}")
    private transient String roleId;

    @ExcelField(value = "角色")
    @ApiModelProperty(value = "角色名", hidden = true)
    private transient String roleName;

    // 排序字段
    @ApiModelProperty(hidden = true)
    private transient String sortField;

    // 排序规则 ascend 升序 descend 降序
    @ApiModelProperty(hidden = true)
    private transient String sortOrder;
    @ApiModelProperty(hidden = true)
    private transient String createTimeFrom;
    @ApiModelProperty(hidden = true)
    private transient String createTimeTo;
    @ApiModelProperty(hidden = true)
    private transient String id;

    /**
     * shiro-redis v3.1.0 必须要有 getAuthCacheKey()或者 getId()方法
     * # Principal id field name. The field which you can get unique id to identify this principal.
     * # For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc.
     * # Remember to add getter to this id field. For example, getUserId(), getUserName(), getEmail(), etc.
     * # Default value is authCacheKey or id, that means your principal object has a method called "getAuthCacheKey()" or "getId()"
     *
     * @return userId as Principal id field name
     */
    @ApiModelProperty(hidden = true)
    public Long getAuthCacheKey() {
        return userId;
    }
}

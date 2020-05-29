package com.ruoyi.javamail.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhangwei
 */
@Data
@TableName("t_user_mail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "用户邮箱对象", description = "")
public class UserMail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户邮箱ID", dataType = "int", hidden = true)
    private Long id;

    @ApiModelProperty(value = "用户ID", required = true, dataType = "int")
    @NotNull(message = "{required}")
    private Long userId;

    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ApiModelProperty(value = "邮箱地址", required = true, dataType = "string")
    @NotBlank(message = "{required}")
    private String mailAddress;

    @ApiModelProperty(value = "邮箱密码", required = true, dataType = "string")
    @NotBlank(message = "{required}")
    private String password;

    @ApiModelProperty(value = "是否是主邮箱，0-主邮箱，1-副邮箱", required = true, dataType = "int")
    @NotNull(message = "{required}")
    @Min(value = 0)
    @Max(value = 1)
    private Integer primaryFlag;

    @ApiModelProperty(value = "是否短信提醒，0-是，1-否", required = true, dataType = "int")
    @NotNull(message = "{required}")
    @Min(value = 0)
    @Max(value = 1)
    private Integer smsFlag;

    @ApiModelProperty(value = "SMTP服务器", required = true, dataType = "string")
    @NotBlank(message = "{required}")
    private String smtp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;


}

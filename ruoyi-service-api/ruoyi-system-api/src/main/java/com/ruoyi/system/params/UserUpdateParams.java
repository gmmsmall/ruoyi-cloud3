package com.ruoyi.system.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@ApiModel(value = "user对象")
@Data
public class UserUpdateParams extends UserParams {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id", example = "123", required = true)
    private Long userId;
}

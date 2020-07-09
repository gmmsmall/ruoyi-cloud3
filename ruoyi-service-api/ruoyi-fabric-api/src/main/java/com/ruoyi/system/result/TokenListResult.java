package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Token对象", description = "")
public class TokenListResult implements Serializable {

    @ApiModelProperty(value = "令牌编号", hidden = true)
    private String tokenNo;

    @ApiModelProperty(value = "令牌编号", hidden = true)
    private String parentNo;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "类型  8查看权限")
    private Integer type;

}

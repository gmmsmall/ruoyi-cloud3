package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PermsResult {

    @ApiModelProperty(value = "权限")
    private String[] perm;

    public PermsResult(String[] perm) {
        this.perm = perm;
    }
}

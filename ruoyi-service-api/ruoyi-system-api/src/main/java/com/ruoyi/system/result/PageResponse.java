package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PageResponse {
    @ApiModelProperty(value = "总数量", example = "10", required = true)
    long total;
    @ApiModelProperty(value = "当前页的数量", example = "3", required = true)
    long size;
}

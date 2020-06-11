package com.ruoyi.fdfs.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("file返回")
public class FileResult {

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("扩展名")
    private String extName;

    @ApiModelProperty("url")
    private String url;
}

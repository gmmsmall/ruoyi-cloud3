package com.ruoyi.acad.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("简历中各子项信息")
public class OnlinePdfEntity {

    @ApiModelProperty("提示语")
    private String remark;//提示语

    @ApiModelProperty("内容")
    private String info;//内容
}

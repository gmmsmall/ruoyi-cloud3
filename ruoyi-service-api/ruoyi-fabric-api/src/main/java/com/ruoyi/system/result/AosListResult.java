package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科学院信息
 *
 * @author xwp
 */
@Getter
@Setter
@ApiModel(value = "区块链研究领域对象", description = "")
public class AosListResult {
    @ApiModelProperty(value = "科学院编号", dataType = "string", hidden = true)
    private String aosNo;            //科学院编号

    @ApiModelProperty(value = "中文名", dataType = "string", required = true)
    private String aosCnname;         //中文名
}

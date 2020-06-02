package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 科学院信息
 *
 * @author xwp
 */
@Data
@ApiModel(value = "科学院")
public class AosResult {

    @ApiModelProperty(value = "科学院编号", dataType = "string", required = true)
    private String aosNo;            //科学院编号

    @ApiModelProperty(value = "中文名", dataType = "string", required = true)
    private String aosCnname;         //中文名

}

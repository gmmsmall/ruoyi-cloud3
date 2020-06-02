package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 科学院信息
 *
 * @author xwp
 */
@Data
@ApiModel(value = "科学院")
public class AosResult {

    //当前用户是否拥有权限
    public static final int IS_CHECK = 1;
    public static final int NO_CHECK = 0;

    @ApiModelProperty(value = "科学院编号", dataType = "string", required = true)
    private String aosNo;            //科学院编号

    @ApiModelProperty(value = "中文名", dataType = "string", required = true)
    private String aosCnname;         //中文名

    @ApiModelProperty(value = "是否拥有权限 0否1是", example = "123")
    private Integer isCheck = 0;

}

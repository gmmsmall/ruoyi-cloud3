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
@ApiModel(value = "院士信息操作日志")
public class AcadOpLogResult {

    @ApiModelProperty(value = "院士姓名", dataType = "string")
    private String acadName;            //科学院编号

    @ApiModelProperty(value = "变动类型", dataType = "string")
    private String operType;

    @ApiModelProperty(value = "操作人员", dataType = "string")
    private String operName;

    @ApiModelProperty(value = "变动内容", dataType = "string")
    private String title;

    @ApiModelProperty(value = "操作时间", dataType = "string")
    private String operTime;

}

package com.ruoyi.system.result;

import com.ruoyi.common.annotation.Excel;
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

    @Excel(name = "院士姓名")
    @ApiModelProperty(value = "院士姓名", dataType = "string")
    private String acadName;            //科学院编号

    @Excel(name = "变动类型")
    @ApiModelProperty(value = "变动类型", dataType = "string")
    private String operType;

    @Excel(name = "操作人员")
    @ApiModelProperty(value = "操作人员", dataType = "string")
    private String operName;

    @Excel(name = "变动内容")
    @ApiModelProperty(value = "变动内容", dataType = "string")
    private String title;

    @Excel(name = "操作时间")
    @ApiModelProperty(value = "操作时间", dataType = "string")
    private String operTime;

}

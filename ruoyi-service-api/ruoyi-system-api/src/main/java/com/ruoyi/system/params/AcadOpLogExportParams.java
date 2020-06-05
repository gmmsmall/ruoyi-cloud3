package com.ruoyi.system.params;

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
public class AcadOpLogExportParams {

    @ApiModelProperty(value = "院士姓名", dataType = "string")
    private String acadName;

    @ApiModelProperty(value = "变动类型 0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据", dataType = "Integer")
    private Integer operType;

    @ApiModelProperty(value = "操作人员", dataType = "string")
    private String operName;

    @ApiModelProperty(value = "开始时间", dataType = "String")
    private String beginTime;

    @ApiModelProperty(value = "结束时间", dataType = "String")
    private String endTime;
}

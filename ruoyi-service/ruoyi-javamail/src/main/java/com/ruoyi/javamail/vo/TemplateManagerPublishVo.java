package com.ruoyi.javamail.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * 已发布的模板信息表
 * @author gmm
 */
@Data
@Excel("模板管理信息表")
@ApiModel(value = "com.ruoyi.javamail.vo.TemplateManagerPublishVo",description = "已发布的模板信息表")
public class TemplateManagerPublishVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 模板名称
     */
    @ExcelField(value = "名称")
    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    /**
     * 模板主题
     */
    @ExcelField(value = "主题")
    @ApiModelProperty(value = "主题", dataType = "string")
    private String topic;

    /**
     * 业务类型 1=问卷调查  2=海外院士行  3=节日问候 4=其他
     */
    @ExcelField(value = "业务类型",writeConverterExp = "1=问卷调查,2=海外院士行,3=节日问候,4=其他")
    @ApiModelProperty(value = "业务类型1=问卷调查,2=海外院士行,3=节日问候,4=其他", dataType = "string")
    private String worktype;

}

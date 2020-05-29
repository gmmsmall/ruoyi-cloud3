package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 模板管理信息表
 * @author gmm
 */
@Data
@TableName("template_manager")
@Excel("模板管理信息表")
@ApiModel(value = "com.ruoyi.javamail.entity.TemplateManager",description = "模板管理信息表")
public class TemplateManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
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
     * 模板类型 1=邮件 2=其他
     */
    @ExcelField(value = "模板类型",writeConverterExp = "1=邮件,2=其他")
    @ApiModelProperty(value = "模板类型1=邮件,2=其他", dataType = "string")
    private String type;

    /**
     * 业务类型 1=问卷调查  2=海外院士行  3=节日问候 4=其他
     */
    @ExcelField(value = "业务类型",writeConverterExp = "1=问卷调查,2=海外院士行,3=节日问候,4=其他")
    @ApiModelProperty(value = "业务类型1=问卷调查,2=海外院士行,3=节日问候,4=其他", dataType = "string")
    private String worktype;
    /**
     * 1=发布 2=未发布
     */
    @ExcelField(value = "稿件状态",writeConverterExp = "1=发布,2=未发布")
    @ApiModelProperty(value = "稿件状态1=发布,2=未发布", dataType = "string")
    private String publish;
    /**
     * 使用次数
     */
    @ExcelField(value = "使用次数")
    @ApiModelProperty(value = "使用次数")
    private Integer usenumber;

    /**
     * 1=有效  2=无效=====》 1是 2否
     */
    @ExcelField(value = "有效",writeConverterExp = "1=是,2=否")
    @ApiModelProperty(value = "有效1=是,2=否", dataType = "string")
    private String effective;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 正文
     */
    @ApiModelProperty(value = "正文")
    private String mainbody;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "新增人")
    private String addperson;

    @ApiModelProperty(value = "新增人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addpersonid;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime edittime;

    /**
     * 修改操作人
     */
    @ApiModelProperty(value = "修改人")
    private String editperson;

    @ApiModelProperty(value = "修改人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long editpersonid;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletetime;

    /**
     * 删除操作人
     */
    @ApiModelProperty(value = "删除人")
    private String deleteperson;

    @ApiModelProperty(value = "删除人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deletepersonid;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除")
    private String deleteflag;

}

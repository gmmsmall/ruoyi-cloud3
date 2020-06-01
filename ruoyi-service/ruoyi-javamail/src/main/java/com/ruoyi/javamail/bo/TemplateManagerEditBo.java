package com.ruoyi.javamail.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 模板管理修改参数实体类
 * @author gmm
 */
@Data
@ApiModel(value = "com.ruoyi.javamail.bo.TemplateManagerEditBo",description = "模板管理修改参数实体类")
public class TemplateManagerEditBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    /**
     * 模板主题
     */
    @ApiModelProperty(value = "主题", dataType = "string")
    private String topic;

    /**
     * 模板类型 1=邮件 2=其他
     */
    @ApiModelProperty(value = "模板类型1=邮件,2=其他", dataType = "string")
    private String type;

    /**
     * 业务类型 1=问卷调查  2=海外院士行  3=节日问候 4=其他
     */
    @ApiModelProperty(value = "业务类型1=问卷调查,2=海外院士行,3=节日问候,4=其他", dataType = "string")
    private String worktype;
    /**
     * 1=发布 2=未发布
     */
    @ApiModelProperty(value = "稿件状态1=发布,2=未发布", dataType = "string")
    private String publish;

    /**
     * 1=有效  2=无效=====》 1是 2否
     */
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

    @ApiModelProperty(value = "修改人id",hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long editpersonid;

    /**
     * 修改操作人
     */
    @ApiModelProperty(value = "修改人姓名",hidden = true)
    private String editperson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime edittime;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除",hidden = true)
    private String deleteflag;

    /**
     * 使用次数
     */
    @ApiModelProperty(value = "使用次数",hidden = true)
    private Integer usenumber;

}

package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * es院士信息表
 */
@Data
@ApiModel(value = "基本信息对象", description = "")
public class BaseInfoEs implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "院士编号", dataType = "Integer")
    @TableId(value = "acad_id")
    private Integer acadId;
    @ApiModelProperty(value = "性别1-男，2-女，3-未知", dataType = "Integer", notes = "1-男，2-女，3-未知")
    private Integer gender;//性别
    @ApiModelProperty(value = "出生日期", dataType = "string")
    private String birthday;//出生日期
    @ApiModelProperty(value = "个人简介原文", dataType = "string")
    private String personalProfileOrig;//个人简介原文
    @ApiModelProperty(value = "个人简介手译", dataType = "string")
    private String personalProfileHand;//个人简介手译
    @ApiModelProperty(value = "个人简介机器翻译", dataType = "string")
    private String personalProfileMechine;//个人简介机器翻译
    /**
     * 8大类
     */
    @ApiModelProperty(value = "研究领域分类，八大类", dataType = "Integer", notes = "八大类")
    private Integer rsfCategory;//研究领域分类
    @ApiModelProperty(value = "研究领域介绍", dataType = "String")
    private String rsfProfile;//研究领域介绍
    @ApiModelProperty(value = "研究领域影响力", dataType = "String")
    private String rsfInfluence;//研究领域影响力
    @ApiModelProperty(value = "产业化潜力预估", dataType = "String")
    private String industEstimate;//产业化潜力预估
    /**
     * 1-已通讯，2-已到访，3-已签约，4-未通讯
     */
    @ApiModelProperty(value = "联络状态1-已通讯，2-已到访，3-已签约，4-未通讯", dataType = "String",notes = "1-已通讯，2-已到访，3-已签约，4-未通讯")
    private Integer contactStatus;

    /**
     * 1-邮箱，2-电话，3-邮箱/电话，4-未联络
     */
    @ApiModelProperty(value = "联络方式1-邮箱，2-电话，3-邮箱/电话，4-未联络", dataType = "String",notes = "1-邮箱，2-电话，3-邮箱/电话，4-未联络")
    private Integer contactMethon;

    /**
     * 1-全职，2-刚性，3-柔性，4-注册，5-其他
     */
    @ApiModelProperty(value = "已签约类型1-全职，2-刚性，3-柔性，4-注册，5-其他", dataType = "String",notes = "1-全职，2-刚性，3-柔性，4-注册，5-其他")
    private Integer signType;
    @ApiModelProperty(value = "是否拉黑0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isBlack;//是否拉黑
    @ApiModelProperty(value = "是否展示0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isShow;//是否展示
    @ApiModelProperty(value = "展厅展示优先级", dataType = "Integer")
    private Integer showValue;//展厅展示优先级
    @ApiModelProperty(value = "是否顶尖院士0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isTop;//是否顶尖院士
    @ApiModelProperty(value = "是否年轻0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isYoung;//是否年轻
    @ApiModelProperty(value = "是否华人0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isChinese;//是否华人
    @ApiModelProperty(value = "生活习惯", dataType = "String")
    private String livingHabit;//生活习惯
    @ApiModelProperty(value = "宗教信仰", dataType = "String")
    private String religion;//宗教信仰
    @ApiModelProperty(value = "籍贯", dataType = "String")
    private String nativePlace;//籍贯
    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;//备注

    @ApiModelProperty(value = "创建者")
    private String createUserId;//创建者

    /*@ApiModelProperty(value = "创建时间")
    private String createTime;//创建时间*/

    @ApiModelProperty(value = "机器还是人工创建")
    private Boolean isMechine;//机器还是人工创建

    @ApiModelProperty(value = "更新人")
    private String updateUserId;//更新人

    /*@ApiModelProperty(value = "更新时间")
    private String updateTime;//更新时间*/
}
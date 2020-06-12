package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description：基本信息表<br/>
 * CreateTime ：2020年3月11日上午10:03:34<br/>
 * CreateUser：ys<br/>
 */
@Data
@TableName("acad_base_info")
@ApiModel(value = "基本信息对象", description = "")
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "院士编号", dataType = "Integer")
    @TableId(value = "acad_id")
    @Field
    private Integer acadId;
    @ApiModelProperty(value = "性别1-男，2-女，3-未知", dataType = "Integer", notes = "1-男，2-女，3-未知")
    @Field
    private Integer gender;//性别
    @ApiModelProperty(value = "出生日期", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Field
    private Date birthday;//出生日期
    @ApiModelProperty("出生日期备注")
    @Field
    private String birthdayRemark;
    @ApiModelProperty(value = "个人简介原文", dataType = "string")
    @Field
    private String personalProfileOrig;//个人简介原文
    @ApiModelProperty(value = "个人简介手译", dataType = "string")
    @Field
    private String personalProfileHand;//个人简介手译
    @ApiModelProperty(value = "个人简介机器翻译", dataType = "string")
    @Field
    private String personalProfileMechine;//个人简介机器翻译

    @ApiModelProperty("真实姓名")
    @Field(fielddata = true)
    private String realName;//真实姓名

    @ApiModelProperty("英文名字")
    @Field
    private String enName;//英文名字

    @ApiModelProperty("中文名字")
    @Field
    private String cnName;//中文名字

    @ApiModelProperty("国籍")
    @Field
    private transient String nationPlace;//国籍（多个国籍拼接而成）

    @ApiModelProperty("科学院")
    @Field
    private transient String aosName;//科学院（多个科学院拼接而成）

    /**
     * 8大类
     */
    @ApiModelProperty(value = "研究领域分类，八大类1-高端装备制造，2-生物医药，3-新能源新材料，4-网络信息，5-设计研发，6-海洋经济，7-军民融合，8-其他", dataType = "Integer", notes = "八大类")
    @Field
    private Integer rsfCategory;//研究领域分类
    @ApiModelProperty(value = "研究领域介绍", dataType = "String")
    @Field
    private String rsfProfile;//研究领域介绍
    @ApiModelProperty(value = "研究领域影响力", dataType = "String")
    @Field
    private String rsfInfluence;//研究领域影响力
    @ApiModelProperty(value = "产业化潜力预估", dataType = "String")
    @Field
    private String industEstimate;//产业化潜力预估
    /**
     * 1-已通讯，2-已到访，3-已签约，4-未通讯
     */
    @ApiModelProperty(value = "联络状态1-已通讯，2-已到访，3-已签约，4-未通讯", dataType = "String",notes = "1-已通讯，2-已到访，3-已签约，4-未通讯")
    @Field
    private Integer contactStatus;

    /**
     * 1-邮箱，2-电话，3-邮箱/电话，4-未联络
     */
    @ApiModelProperty(value = "联络方式1-邮箱，2-电话，3-邮箱/电话，4-未联络", dataType = "String",notes = "1-邮箱，2-电话，3-邮箱/电话，4-未联络")
    @Field
    private Integer contactMethon;

    /**
     * 1-全职，2-刚性，3-柔性，4-注册，5-其他
     */
    @ApiModelProperty(value = "已签约类型1-全职，2-刚性，3-柔性，4-注册，5-其他", dataType = "String",notes = "1-全职，2-刚性，3-柔性，4-注册，5-其他")
    @Field
    private Integer signType;
    @ApiModelProperty(value = "是否拉黑0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @Field
    private Boolean isBlack;//是否拉黑
    @ApiModelProperty(value = "是否展示0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @Field
    private Boolean isShow;//是否展示
    @ApiModelProperty(value = "展厅展示优先级", dataType = "Integer")
    @Field
    private Integer showValue;//展厅展示优先级
    @ApiModelProperty(value = "是否顶尖院士0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @Field
    private Boolean isTop;//是否顶尖院士
    @ApiModelProperty(value = "是否年轻0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @Field
    private Boolean isYoung;//是否年轻
    @ApiModelProperty(value = "是否华人0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @Field
    private Boolean isChinese;//是否华人
    @ApiModelProperty(value = "生活习惯", dataType = "String")
    @Field
    private String livingHabit;//生活习惯
    @ApiModelProperty(value = "宗教信仰", dataType = "String")
    @Field
    private String religion;//宗教信仰
    @ApiModelProperty(value = "籍贯", dataType = "String")
    @Field
    private String nativePlace;//籍贯
    @ApiModelProperty(value = "备注", dataType = "String")
    @Field
    private String remark;//备注

    @ApiModelProperty(value = "创建者")
    @Field
    private String createUserId;//创建者

    @ApiModelProperty(value = "创建时间",hidden = true)
    @Field
    private Date createTime;//创建时间

    @ApiModelProperty(value = "机器还是人工创建")
    @Field
    private Boolean isMechine;//机器还是人工创建

    @ApiModelProperty(value = "更新人")
    @Field
    private String updateUserId;//更新人

    @ApiModelProperty(value = "更新时间",hidden = true)
    @Field
    private Date updateTime;//更新时间

    //0-已删除，1-未删除
    @ApiModelProperty("是否删除")
    private Boolean delFlag;

    @ApiModelProperty("删除操作人")
    private Long delUserId;

    @ApiModelProperty("删除时间")
    private Date delDatetime;
}
package com.ruoyi.acad.form;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description：院士信息列表展示实体类<br/>
 * CreateTime ：2020年6月9日下午14:32:30<br/>
 * CreateUser：gmm<br/>
 */
@Data
public class BaseInfoShowForm implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "院士编号", dataType = "Integer")
    private Integer acadId;

    @ApiModelProperty(value = "头像")
    private String photo;//头像

    @ApiModelProperty("姓名")
    private String acadName;//院士姓名，显示顺序为真实姓名、中文名字。英文名字

    @ApiModelProperty(value = "出生日期")
    private String birthday;//出生日期

    @ApiModelProperty("国籍")
    private String nationPlace;//国籍（多个国籍拼接而成）

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;//籍贯

    @ApiModelProperty("授衔机构")
    private String aosName;//科学院（显示正籍的科学院）

    @ApiModelProperty("邮箱")
    private String email;//只显示有效的主邮箱


    /**
     * 8大类
     */
    @ApiModelProperty(value = "研究领域分类，八大类专业领域1-高端装备制造，2-生物医药，3-新能源新材料，4-网络信息，5-设计研发，6-海洋经济，7-军民融合，8-其他", dataType = "Integer", notes = "八大类")
    private Integer rsfCategory;//研究领域分类

    /**
     * 1-邮箱，2-电话，3-邮箱/电话，4-未联络
     */
    @ApiModelProperty(value = "联络方式1-邮箱，2-电话，3-邮箱/电话，4-未联络", dataType = "String",notes = "1-邮箱，2-电话，3-邮箱/电话，4-未联络")
    private Integer contactMethon;

    /**
     * 1-全职，2-刚性，3-柔性，4-注册，5-其他
     */
    @ApiModelProperty(value = "已签约类型1-刚性，2-全职，3-柔性，4-注册，5-其他", dataType = "String",notes = "1-刚性，2-全职，3-柔性，4-注册，5-其他")
    private Integer signType;

    @ApiModelProperty(value = "是否拉黑0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isBlack;//是否拉黑

    @ApiModelProperty(value = "是否展示0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    private Boolean isShow;//是否展示

}
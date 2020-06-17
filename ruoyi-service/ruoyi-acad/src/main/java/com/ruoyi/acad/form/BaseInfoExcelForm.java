package com.ruoyi.acad.form;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description：院士信息列表导出实体类<br/>
 * CreateTime ：2020年6月16日下午17:39:30<br/>
 * CreateUser：gmm<br/>
 */
@Data
@Excel("院士信息表")
public class BaseInfoExcelForm implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "院士编号", dataType = "Integer")
    @ExcelField(value = "院士编号")
    private Integer acadId;

    @ApiModelProperty(value = "头像")
    @ExcelField(value = "头像")
    private String photo;//头像

    @ApiModelProperty("姓名")
    @ExcelField(value = "姓名")
    private String acadName;//院士姓名，显示顺序为真实姓名、中文名字。英文名字

    @ApiModelProperty(value = "出生日期")
    @ExcelField(value = "出生日期")
    private String birthday;//出生日期

    @ApiModelProperty("国籍")
    @ExcelField(value = "国籍")
    private String nationPlace;//国籍（多个国籍拼接而成）

    @ApiModelProperty(value = "籍贯")
    @ExcelField(value = "籍贯")
    private String nativePlace;//籍贯

    @ApiModelProperty("授衔机构")
    @ExcelField(value = "授衔机构")
    private String aosName;//科学院（显示正籍的科学院）

    @ApiModelProperty("邮箱")
    @ExcelField(value = "邮箱")
    private String email;//只显示有效的主邮箱


    /**
     * 8大类
     */
    @ApiModelProperty(value = "研究领域分类，八大类专业领域1-高端装备制造，2-生物医药，3-新能源新材料，4-网络信息，5-设计研发，6-海洋经济，7-军民融合，8-其他", dataType = "Integer", notes = "八大类")
    @ExcelField(value = "研究领域分类",writeConverterExp = "1=高端装备制造,2=生物医药,3=新能源新材料,4=网络信息,5=设计研发,6=海洋经济,7=军民融合,8=其他")
    private Integer rsfCategory;//研究领域分类

    /**
     * 1-邮箱，2-电话，3-邮箱/电话，4-未联络
     */
    @ApiModelProperty(value = "联络方式1-邮箱，2-电话，3-邮箱/电话，4-未联络", dataType = "String",notes = "1-邮箱，2-电话，3-邮箱/电话，4-未联络")
    @ExcelField(value = "联络方式",writeConverterExp ="1=邮箱,2=电话,3=邮箱/电话,4=未联络")
    private Integer contactMethon;

    /**
     * 1-全职，2-刚性，3-柔性，4-注册，5-其他
     */
    @ApiModelProperty(value = "已签约类型1-全职，2-刚性，3-柔性，4-注册，5-其他", dataType = "String",notes = "1-全职，2-刚性，3-柔性，4-注册，5-其他")
    @ExcelField(value = "已签约类型",writeConverterExp = "1=全职,2=刚性,3=柔性,4=注册,5=其他")
    private Integer signType;

    @ApiModelProperty(value = "是否拉黑0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @ExcelField(value = "是否拉黑",writeConverterExp = "0=否,1=是")
    private Boolean isBlack;//是否拉黑

    @ApiModelProperty(value = "是否展示0-否，1-是", dataType = "Boolean",notes = "0-否，1-是")
    @ExcelField(value = "是否展示",writeConverterExp ="0=否,1=是")
    private Boolean isShow;//是否展示

    @ApiModelProperty(value = "展厅展示优先级")
    @ExcelField(value = "展厅展示优先级")
    private Integer showValue;//展厅展示优先级

    @ApiModelProperty(value = "是否顶尖院士")
    @ExcelField(value = "是否顶尖院士",writeConverterExp ="0=否,1=是")
    private Boolean isTop;//是否顶尖院士

    @ApiModelProperty(value = "是否年轻")
    @ExcelField(value = "是否年轻",writeConverterExp ="0=否,1=是")
    private Boolean isYoung;//是否年轻

    @ApiModelProperty(value = "是否华人")
    @ExcelField(value = "是否华人",writeConverterExp ="0=否,1=是")
    private Boolean isChinese;//是否华人

    //有效的主电话
    @ApiModelProperty("电话号码")
    @ExcelField(value = "电话号码")
    private String phoneNumber;//电话号码

    //学校拼接的字符串
    @ApiModelProperty("教育信息")
    @ExcelField(value = "教育信息")
    private String school;//学校

    @ApiModelProperty("工作单位(原文)")
    @ExcelField(value = "工作单位(原文)")
    private String workUnit;//工作单位

    @ApiModelProperty("荣誉信息")
    @ExcelField(value = "荣誉信息")
    private String awardName;//项目名称


    @ApiModelProperty("论文信息")
    @ExcelField(value = "论文信息")
    private String paperTitle;//论文题目

    @ApiModelProperty("专利信息")
    @ExcelField(value = "专利信息")
    private String patentName;

    @ApiModelProperty(value = "生活习惯")
    @ExcelField(value = "生活习惯")
    private String livingHabit;//生活习惯

    @ApiModelProperty(value = "宗教信仰")
    @ExcelField(value = "宗教信仰")
    private String religion;//宗教信仰

    @ApiModelProperty(value = "备注")
    @ExcelField(value = "备注")
    private String remark;//备注

    @ApiModelProperty(value = "个人简介原文")
    @ExcelField(value = "个人简介原文")
    private String personalProfileOrig;//个人简介原文
}
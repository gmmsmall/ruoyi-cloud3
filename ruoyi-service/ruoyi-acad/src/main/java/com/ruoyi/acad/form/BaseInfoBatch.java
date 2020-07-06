package com.ruoyi.acad.form;

import com.ruoyi.acad.domain.Nationality;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description：批量修改的院士基本信息表<br/>
 * CreateTime ：2020年6月12日下午16:50:00<br/>
 * CreateUser：gmm<br/>
 */
@Data
@ApiModel(value = "批量修改的院士基本信息表")
public class BaseInfoBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "院士编号列表", dataType = "list")
    private List<BaseInfoAcadIdIntegerForm> list;

    /**
     * 8大类
     */
    @ApiModelProperty(value = "研究领域分类", dataType = "Integer", notes = "八大类")
    private Integer rsfCategory;//研究领域分类

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;//籍贯

    /*@ApiModelProperty("国籍")
    private transient String nationPlace;//国籍（多个国籍拼接而成）*/

    @ApiModelProperty("国籍列表")
    private List<Nationality> nationalityList;

    /**
     * 1-已通讯，2-已到访，3-已签约，4-未通讯
     */
    @ApiModelProperty(value = "联络状态")
    private Integer contactStatus;

    /**
     * 1-邮箱，2-电话，3-邮箱/电话，4-未联络
     */
    @ApiModelProperty(value = "联络方式")
    private Integer contactMethon;

    /**
     * 1-刚性，2-全职，3-柔性，4-注册，5-其他
     */
    @ApiModelProperty(value = "已签约类型")
    private Integer signType;

    @ApiModelProperty(value = "是否华人")
    private Boolean isChinese;//是否华人

    @ApiModelProperty(value = "是否顶尖院士")
    private Boolean isTop;//是否顶尖院士

    @ApiModelProperty(value = "是否年轻")
    private Boolean isYoung;//是否年轻

    @ApiModelProperty(value = "是否拉黑")
    private Boolean isBlack;//是否拉黑

    @ApiModelProperty(value = "是否展示")
    private Boolean isShow;//是否展示

    @ApiModelProperty(value = "展厅展示优先级")
    private Integer showValue;//展厅展示优先级

    @ApiModelProperty(value = "备注")
    private String remark;//备注

    @ApiModelProperty(value = "标签", dataType = "String")
    private String acadLabel;

    @ApiModelProperty(value = "更新人",hidden = true)
    private String updateUserId;//更新人

    @ApiModelProperty(value = "更新时间",hidden = true)
    private Date updateTime;//更新时间

}
package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author jxd
 */
@Data
@TableName("send_mail_items")
@Excel("邮件详情信息表")
@ApiModel
public class SendMailItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    private Long id;//主键

    /**
     * 主表主键
     */
    @ApiModelProperty(value = "fid", hidden = true)
    private Long fid;

    /**
     * 姓名
     */
    @ExcelField(value = "姓名")
    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    /**
     * 国籍
     */
    @ExcelField(value = "国籍")
    @ApiModelProperty(value = "国籍", dataType = "string")
    private String nationality;

    /**
     * 授衔机构
     */
    @ExcelField(value = "授衔机构")
    @ApiModelProperty(value = "organization", hidden = true)
    private String organization;

    /**
     * 签约情况
     */
    @ExcelField(value = "签约情况",writeConverterExp = "1=已签约,2=未签约")
    @ApiModelProperty(value = "sign", hidden = true)
    private String sign;

    /**
     * 到访情况
     */
    @ExcelField(value = "到访情况",writeConverterExp = "1=已到访,2=未到访")
    @ApiModelProperty(value = "visit", hidden = true)
    private String visit;

    /**
     * 是否发送成功,1是 2，否
     */
    @ApiModelProperty(value = "是否成功", dataType = "string")
    @ExcelField(value = "此次发送状态",writeConverterExp = "1=是,2=否")
    private String sendflag;

    /**
     * 总体上表示邮件是否发送成功，1成功2失败
     */
    @ExcelField(value = "总体发送状态",writeConverterExp = "1=是,2=否")
    @ApiModelProperty(value = "majorsendflag", hidden = true)
    private String majorsendflag;

    /**
     * 邮箱地址
     */
    @ExcelField(value = "邮箱地址")
    @ApiModelProperty(value = "邮箱地址", dataType = "string")
    private String showemail;//显示邮箱，用于在前台页面进行显示的

    @ApiModelProperty(value = "email", hidden = true)
    private String email;

    @ApiModelProperty(value = "nextemail", hidden = true)
    private String nextemail;//副邮箱

    /**
     * 引才负责人
     */
    @ExcelField(value = "引才负责人")
    @ApiModelProperty(value = "leader", hidden = true)
    private String leader;

    @ApiModelProperty(value = "leaderid", hidden = true)
    private Long leaderid;

    /**
     * 院士编码
     */
    @ApiModelProperty(value = "acadencode", hidden = true)
    private String acadencode;

    @ApiModelProperty(value = "failreason", hidden = true)
    private String failreason;//发送失败原因

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "addtime", hidden = true)
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "addperson", hidden = true)
    private String addperson;

    @ApiModelProperty(value = "addpersonid", hidden = true)
    private Long addpersonid;

    /**
     * 邮箱类型，1=主邮箱   2=副邮箱
     */
    @ApiModelProperty(value = "type", hidden = true)
    private String type;

    /**
     * 总体上表示邮件是否发送成功，1成功2失败
     */
    //private String majorsendflag;

    //private String showemail;//显示邮箱，用于在前台页面进行显示的

    @ApiModelProperty(value = "sendemail", hidden = true)
    private String sendemail;//发件邮箱，为了查询时更方便

}

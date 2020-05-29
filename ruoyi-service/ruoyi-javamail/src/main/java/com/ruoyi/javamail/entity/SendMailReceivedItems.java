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
 * @author gmm
 */
@Data
@TableName("send_mail_received_items")
@Excel("主题邮件接收子表信息表")
@ApiModel
public class SendMailReceivedItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    private Long id;//主键

    /**
     * 主表主键
     */
    @ApiModelProperty(value = "主表主键", dataType = "string")
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
    @ApiModelProperty(value = "签约情况", dataType = "string")
    private String sign;

    /**
     * 到访情况
     */
    @ExcelField(value = "到访情况",writeConverterExp = "1=已到访,2=未到访")
    @ApiModelProperty(value = "到访情况", dataType = "string")
    private String visit;

    /**
     * 发件次数
     */
    @ExcelField(value = "发件次数")
    @ApiModelProperty(value = "sendnumber", hidden = true)
    private Integer sendnumber;

    /**
     * 回件次数
     */
    @ExcelField(value = "回件次数")
    @ApiModelProperty(value = "receivenumber", hidden = true)
    private Integer receivenumber;

    /**
     * 回件邮箱
     */
    @ExcelField(value = "回件邮箱")
    @ApiModelProperty(value = "回件邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "leaderid", hidden = true)
    private Long leaderid;

    /**
     * 引才负责人
     */
    @ExcelField(value = "引才负责人")
    @ApiModelProperty(value = "leader", hidden = true)
    private String leader;

    /**
     * 新增时间
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
     * 院士编码
     */
    @ApiModelProperty(value = "acadencode", hidden = true)
    private String acadencode;

    @ApiModelProperty(value = "sendemail", hidden = true)
    private String sendemail;//发件邮箱，为了查询时更方便


}

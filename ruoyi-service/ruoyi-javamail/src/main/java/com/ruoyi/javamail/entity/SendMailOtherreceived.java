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
@TableName("send_mail_otherreceived")
@Excel("其他邮件接收信息表")
@ApiModel
public class SendMailOtherreceived implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    private Long id;//主键

    /**
     * 发件主题
     */
    @ExcelField(value = "发件主题")
    @ApiModelProperty(value = "发件主题", dataType = "string")
    private String mailtopic;

    /**
     * 接收时间
     */
    @ExcelField(value = "接收时间")
    @ApiModelProperty(value = "receivetime", hidden = true)
    private LocalDateTime receivetime;

    /**
     * 发件邮箱
     */
    @ExcelField(value = "发件邮箱")
    @ApiModelProperty(value = "发件邮箱", dataType = "string")
    private String sendemail;

    /**
     * 是否短信提醒 0是 1 否
     */
    @ExcelField(value = "是否短信提醒",writeConverterExp = "0=是,1=否")
    @ApiModelProperty(value = "是否短信提醒", dataType = "string")
    private String isnote;

    /**
     * 收件邮箱
     */
    @ExcelField(value = "收件邮箱")
    @ApiModelProperty(value = "receiveemail", hidden = true)
    private String receiveemail;

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

    @ApiModelProperty(value = "开始日期", dataType = "string")
    private transient String createTimeFrom;
    @ApiModelProperty(value = "结束日期", dataType = "string")
    private transient String createTimeTo;


}

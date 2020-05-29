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
@TableName("send_mail_received")
@Excel("主题邮件接收信息表")
@ApiModel
public class SendMailReceived implements Serializable {

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
     * 发件名称
     */
    @ExcelField(value = "发件名称")
    @ApiModelProperty(value = "发件名称", dataType = "string")
    private String mailname;

    /**
     * 本次发件量
     */
    @ExcelField(value = "本次发件量")
    @ApiModelProperty(value = "发件数量", dataType = "string")
    private Integer sendnumber;

    /**
     * 收件量
     */
    @ExcelField(value = "收件量")
    @ApiModelProperty(value = "receivenumber", hidden = true)
    private Integer receivenumber;

    /**
     * 发件箱
     */
    @ApiModelProperty(value = "sendbox", hidden = true)
    private String sendbox;

    /**
     * stmp服务器
     */
    @ApiModelProperty(value = "stmp", hidden = true)
    private String stmp;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "addtime", hidden = true)
    @ExcelField(value = "发送时间")
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "addperson", hidden = true)
    @ExcelField(value = "操作人")
    private String addperson;

    @ApiModelProperty(value = "addpersonid", hidden = true)
    private Long addpersonid;

    @ApiModelProperty(value = "开始日期", dataType = "string")
    private transient String createTimeFrom;
    @ApiModelProperty(value = "结束日期", dataType = "string")
    private transient String createTimeTo;


}

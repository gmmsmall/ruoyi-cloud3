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
@TableName("send_mail")
@Excel("邮件发送信息表")
@ApiModel
public class SendMail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    private Long id;//主键

    @ApiModelProperty(value = "templateid", hidden = true)
    private Long templateid;//模板id

    @ApiModelProperty(value = "stmp", hidden = true)
    private String stmp;//stmp服务器

    /**
     * 发件主题
     */
    @ApiModelProperty(value = "发件主题", dataType = "string")
    @ExcelField(value = "发件主题")
    private String mailtopic;

    /**
     * 发件名称
     */
    @ApiModelProperty(value = "发件名称", dataType = "string")
    @ExcelField(value = "发件名称")
    private String mailname;

    /**
     * 本次发件量
     */
    @ApiModelProperty(value = "发件数量", dataType = "integer")
    @ExcelField(value = "本次发件量")
    private Integer sendnumber;

    /**
     * 本次发送成功量
     */
    @ApiModelProperty(value = "successnumber", hidden = true)
    @ExcelField(value = "本次发送成功量")
    private Integer successnumber;

    /**
     * 本次发送失败量
     */
    @ApiModelProperty(value = "failnumber", hidden = true)
    @ExcelField(value = "本次发送失败量")
    private Integer failnumber;

    /**
     * 发送时间
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

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "deletetime", hidden = true)
    private LocalDateTime deletetime;

    /**
     * 删除操作人
     */
    @ApiModelProperty(value = "deleteperson", hidden = true)
    private String deleteperson;

    @ApiModelProperty(value = "deletepersonid", hidden = true)
    private Long deletepersonid;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "deleteflag", hidden = true)
    private String deleteflag;

    /**
     * 发件箱
     */
    @ApiModelProperty(value = "sendbox", hidden = true)
    private String sendbox;

    @ApiModelProperty(value = "mailpassword", hidden = true)
    private String mailpassword;//发件箱密码

    @ApiModelProperty(value = "开始日期", dataType = "string")
    private transient String createTimeFrom;

    @ApiModelProperty(value = "结束日期", dataType = "string")
    private transient String createTimeTo;

}

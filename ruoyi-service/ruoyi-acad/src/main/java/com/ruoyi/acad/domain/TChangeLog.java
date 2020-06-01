package com.ruoyi.acad.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("院士信息日志表")
public class TChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    //变动类型-创建
    public static final long CHANGE_TYPE_INSERT = 1;
    //变动类型-修改
    public static final long CHANGE_TYPE_UPDATE = 2;
    //变动类型-删除
    public static final long CHANGE_TYPE_DELETE = 3;

    /**
     * 院士姓名
     */
    @ExcelField(value = "院士姓名")
    private String acadName;

    /**
     * 变动类型
     */
    @ExcelField(value = "变动类型")
    private Long changeType;

    /**
     * 操作人员
     */
    @ExcelField(value = "操作人员")
    private Long changeUser;

    /**
     * 变动内容
     */
    @ExcelField(value = "变动内容")
    private String changeDetail;

    /**
     * 操作时间
     */
    @ExcelField(value = "操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changeTime;


}

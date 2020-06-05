package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 操作日志记录表 acad_log
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("院士信息日志")
public class AcadOperLog extends BaseEntity {
    //
    private static final long serialVersionUID = -5556121284445360558L;

    /**
     * 日志主键
     */
    @Excel(name = "操作序号")
    private Long operId;

    /**
     * 院士id
     */
    @ApiModelProperty(value = "院士id", dataType = "string")
    private Long acadId;

    private String acadIds;

    /**
     * 操作模块
     */
    @Excel(name = "操作模块")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4拉黑 5展示）
     */
    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除 4拉黑 5展示）", dataType = "integer")
    @Excel(name = "业务类型", readConverterExp = "0其它 1新增 2修改 3删除 4拉黑 5展示")
    private Integer businessType;

    /**
     * 操作人员
     */
    @Excel(name = "操作人员")
    @ApiModelProperty(value = "操作人员", dataType = "string")
    private Long opUserId;

    private String opUserIds;

    /**
     * 操作时间
     */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    private String limit;
}

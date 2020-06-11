package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *简历实体类
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "简历实体类")
@TableName("online_resume")
public class OnlineResume implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 院士编码
     */
    @ApiModelProperty(value = "院士编码", dataType = "string")
    private String acadecode;

    /**
     * 院士姓名
     */
    @ApiModelProperty(value = "院士姓名", dataType = "string")
    private String acadename;

    /**
     * 简历地址
     */
    @ApiModelProperty(value = "简历地址", dataType = "string")
    private String resumeurl;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间", hidden = true)
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "操作人", dataType = "string")
    private String addperson;

    @ApiModelProperty(value = "操作人id", hidden = true)
    private Long addpersonid;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间", hidden = true)
    private LocalDateTime deletetime;

    /**
     * 删除操作人
     */
    @ApiModelProperty(value = "删除操作人", hidden = true)
    private String deleteperson;

    @ApiModelProperty(value = "删除操作人id", hidden = true)
    private Long deletepersonid;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除", hidden = true)
    private String deleteflag;


}

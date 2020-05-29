package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OnlineResume implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
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
    @ApiModelProperty(value = "addtime", hidden = true)
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "操作人", dataType = "string")
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


}

package com.ruoyi.javamail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * 分组管理标准表
 * @author gmm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "com.ruoyi.javamail.entity.SendMailGroup",description = "分组管理标准表")
public class SendMailGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", dataType = "string")
    private String name;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addtime;

    /**
     * 新增操作人
     */
    @ApiModelProperty(value = "新增人", hidden = true)
    private String addperson;

    @ApiModelProperty(value = "新增人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addpersonid;

    /**
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "删除标记 1=未删除  2= 删除", hidden = true)
    private String deleteflag;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletetime;

    /**
     * 删除操作人
     */
    @ApiModelProperty(value = "删除人", hidden = true)
    private String deleteperson;

    @ApiModelProperty(value = "删除人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deletepersonid;

    /**
     * 分组子表，即每个分组下面包含多少院士
     */
  /*  private List<SendMailGroupItems> itemsList;*/

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime edittime;

    /**
     * 修改操作人
     */
    @ApiModelProperty(value = "修改人", hidden = true)
    private String editperson;

    @ApiModelProperty(value = "修改人id", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long editpersonid;


}

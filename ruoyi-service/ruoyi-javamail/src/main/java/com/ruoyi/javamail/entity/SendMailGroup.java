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
public class SendMailGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    private Long id;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", dataType = "string")
    private String name;

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
     * 删除标记 1=未删除  2= 删除
     */
    @ApiModelProperty(value = "deleteflag", hidden = true)
    private String deleteflag;

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
     * 分组子表，即每个分组下面包含多少院士
     */
  /*  private List<SendMailGroupItems> itemsList;*/

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "edittime", hidden = true)
    private LocalDateTime edittime;

    /**
     * 修改操作人
     */
    @ApiModelProperty(value = "editperson", hidden = true)
    private String editperson;

    @ApiModelProperty(value = "editpersonid", hidden = true)
    private Long editpersonid;


}

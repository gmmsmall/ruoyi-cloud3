package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士附件表<br/>
 * CreateTime ：2020年3月11日上午10:01:14<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_attachment")
@ApiModel(value = "院士附件信息表")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer acadId;

    @ApiModelProperty("附件id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attachmentId;//附件ID

    @ApiModelProperty("附件名称")
    private String attachmentName;//附件名称

    @ApiModelProperty("附件地址")
    private String attachmentUrl;//附件地址

    @ApiModelProperty("附件类型")
    private String extName;

    @ApiModelProperty("上传用户")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uploadUserId;//上传用户

}

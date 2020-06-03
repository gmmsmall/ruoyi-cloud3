package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description：院士照片表<br/>
 * CreateTime ：2020年3月11日上午10:08:02<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_photo")
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("院士编码")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private Long acadId;

    @ApiModelProperty("照片id")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull
    private Long photoId;

    @ApiModelProperty("照片url")
    private String photoUrl;

    @ApiModelProperty("是否为展厅照片")
    private Boolean isHall;

    @ApiModelProperty(value = "性别1-男，2-女，3-未知", dataType = "Integer", notes = "1-男，2-女，3-未知")
    private Integer aiGender;

    @ApiModelProperty("更新时间")
    private LocalDate aiDatetime;//更新时间

    @ApiModelProperty("操作人id")
    private Long uploadUserId;

    @ApiModelProperty("操作时间")
    private LocalDateTime uploadDatetime;

    //0-已删除，1-未删除
    @ApiModelProperty("是否删除")
    private Boolean delFlag;

    @ApiModelProperty("删除操作人")
    private Long delUserId;

    @ApiModelProperty("删除时间")
    private LocalDateTime delDatetime;

}

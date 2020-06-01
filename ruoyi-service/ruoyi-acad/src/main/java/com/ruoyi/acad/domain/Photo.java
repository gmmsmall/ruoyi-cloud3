package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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

    private long acadId;

    private long photoId;

    private Boolean isHall;

    @ApiModelProperty(value = "性别", dataType = "Integer", notes = "1-男，2-女，3-未知")
    private Integer aiGender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate aiDatetime;//更新时间

    private long uploadUserId;

    private long uploadDatetime;

    //0-已删除，1-未删除
    private Boolean delFlag;

    private long delUserId;

}

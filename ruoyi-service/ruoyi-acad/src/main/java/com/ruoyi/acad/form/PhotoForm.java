package com.ruoyi.acad.form;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class PhotoForm implements Serializable {

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

}

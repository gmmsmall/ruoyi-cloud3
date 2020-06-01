package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士工作表<br/>
 * CreateTime ：2020年3月11日上午10:08:50<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_work")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String workUnit;//工作单位

    private String jobTitle;//职务

    private LocalDateTime jobStartYear;//工作开始时间

    private LocalDateTime jobEndYear;//工作结束时间
}

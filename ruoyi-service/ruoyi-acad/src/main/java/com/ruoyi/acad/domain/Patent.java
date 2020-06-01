package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Description：院士专利表<br/>
 * CreateTime ：2020年3月11日上午10:07:41<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_patent")
public class Patent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String patentId;

    private String patentName;

    private LocalDate getTime;

    private String patentWebsite;

}
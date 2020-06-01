package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士获奖情况表<br/>
 * CreateTime ：2020年3月11日上午10:02:55<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_award")
public class Award implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String awardName;//项目名称

    private String awardCategory;//奖项类别名称

    private LocalDateTime awardYear;//获奖时间

    private String awardProfile;//获奖介绍
}
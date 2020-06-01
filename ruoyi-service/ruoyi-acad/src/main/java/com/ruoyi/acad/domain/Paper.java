package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Description：院士论文表<br/>
 * CreateTime ：2020年3月11日上午10:07:09<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_paper")
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String paperId;//论文ID

    private String paperTitle;//论文题目

    private String paperAbstract;//论文摘要

    private LocalDate publishedTime;//发表时间

    /**
     * 1-SCI，2-EI，3-IEEE，4-其他
     */
    private Integer periodical;//论文类别

    private String paperWebsiteLink;//网页链接

    private Integer hIndex;//引用数量


}

package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士科学院对应关系表<br/>
 * CreateTime ：2020年3月11日上午10:00:41<br/>
 * CreateUser：ys<br/>
 */
@Data
@TableName("acad_aos")
public class Aos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private Integer aosId;//科学院

    private LocalDateTime electedYear;//当选年

    /**
     * 1-正籍，2-外籍，3-通讯，4-其他
     */
    private Integer aosMemberType;

    private String acadWebsiteLink;//科学院院士网页地址

    private String acadWebsiteSnapshot;//科学院院士网页地址快照
}
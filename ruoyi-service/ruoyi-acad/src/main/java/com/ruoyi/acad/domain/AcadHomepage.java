package com.ruoyi.acad.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：个人主页<br/>
 * CreateTime ：2020年3月31日下午2:26:42<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AcadHomepage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String personalHomepage;//个人网站

    private String personalHomepageSnapshot;//个人网站快照

    private String labWebsiteLink;//个人实验室网站

    private String labWebsiteSnap;//个人实验室网页快照


}

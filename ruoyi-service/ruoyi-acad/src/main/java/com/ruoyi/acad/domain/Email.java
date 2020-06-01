package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士邮箱表<br/>
 * CreateTime ：2020年3月11日上午10:04:20<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_email")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String email;

    private Boolean isMainEmail;

    private Boolean isEffectiveEmail;//是否为有效邮箱


}

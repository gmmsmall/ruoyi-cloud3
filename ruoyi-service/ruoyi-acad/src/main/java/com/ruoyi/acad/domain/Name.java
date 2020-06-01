package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士姓名表<br/>
 * CreateTime ：2020年3月11日上午10:05:37<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_name")
public class Name implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String rawName;//网站原始姓名

    private String realName;//真实姓名

    private String enName;//英文名字

    private String cnName;//中文名字
}

package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士联系信息表<br/>
 * CreateTime ：2020年3月11日上午10:08:28<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_sns")
public class Sns implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private String snsRemark;//联系方式名称

    private String snsValue;//联系方式

}

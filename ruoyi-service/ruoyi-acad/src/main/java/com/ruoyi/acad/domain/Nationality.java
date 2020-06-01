package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：院士国籍对应关系表<br/>
 * CreateTime ：2020年3月11日上午10:05:54<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_nationality")
public class Nationality implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    private Integer countryId;


}

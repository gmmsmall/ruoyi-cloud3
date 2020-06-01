package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description：院士教育履历表<br/>
 * CreateTime ：2020年3月11日上午10:03:45<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_education")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer acadId;

    /**
     * 1-学士，2-硕士，3-博士，4-未知
     */
    private Integer education;

    private String school;//学校

    private LocalDateTime graduationYear;//毕业时间
}

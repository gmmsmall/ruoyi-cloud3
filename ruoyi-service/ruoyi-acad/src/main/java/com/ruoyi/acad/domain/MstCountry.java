package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：国家地区表<br/>
 * CreateTime ：2020年3月11日上午10:05:24<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_mst_country")
public class MstCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer continentId;

    private String continentName;

    private String continentShortName;

    private Integer countryId;

    private String countryName;

    private String countryShortName;


}

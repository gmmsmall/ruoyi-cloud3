package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("大洲id")
    private Integer continentId;

    @ApiModelProperty("大洲名称")
    private String continentName;

    @ApiModelProperty("大洲简称")
    private String continentShortName;

    @ApiModelProperty("国家id")
    private Integer countryId;

    @ApiModelProperty("国家中文名称")
    private String countryCnname;

    @ApiModelProperty("国家英文名称")
    private String countryEnname;

    @ApiModelProperty("国家简称")
    private String countryShortName;


}

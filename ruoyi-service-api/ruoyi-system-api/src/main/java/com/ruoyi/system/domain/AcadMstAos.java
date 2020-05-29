package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 *
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("科学院model")
public class AcadMstAos implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "科学院ID" , dataType = "string" , required = true)
    private Integer aosId;

    @ApiModelProperty(value = "国家ID" , dataType = "string" , required = true)
    private String countryId;

    @ApiModelProperty(value = "科学院中文名" , dataType = "string" , required = true)
    private String aosCnname;

    @ApiModelProperty(value = "科学院英文名" , dataType = "string" , required = true)
    private String aosEnname;

    @ApiModelProperty(value = "科学院主页" , dataType = "string" , required = true)
    private String aosHomePage;

    @ApiModelProperty(value = "科学院Logo" , dataType = "string" , required = true)
    private String aosLogoUrl;

    @ApiModelProperty(value = "子科学院ID" , dataType = "string" , required = true)
    private Integer subAosId;

    @ApiModelProperty(value = "子科学院中文名" , dataType = "string" , required = true)
    private String subAosCnname;

    @ApiModelProperty(value = "子科学院英文名" , dataType = "string" , required = true)
    private String subAosEnname;

    @ApiModelProperty(value = "子科学院主页" , dataType = "string" , required = true)
    private String subAosHomePage;

    @ApiModelProperty(value = "子科学院Logo" , dataType = "string" , required = true)
    private String subAosLogoUrl;

    @ApiModelProperty(value = "令牌" , dataType = "string" , hidden = true)
    private String aosNo;


}

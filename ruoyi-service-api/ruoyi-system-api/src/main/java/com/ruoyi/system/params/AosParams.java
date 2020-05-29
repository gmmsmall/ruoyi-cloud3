package com.ruoyi.system.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("科学院新增")
public class AosParams {

    @ApiModelProperty(value = "科学院令牌", dataType = "string", hidden= true)
    private String aosNo;

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

}

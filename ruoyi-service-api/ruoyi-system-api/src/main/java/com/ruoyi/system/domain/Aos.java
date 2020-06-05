package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科学院信息
 *
 * @author xwp
 */
@Getter
@Setter
@ApiModel(value = "区块链研究领域对象", description = "")
public class Aos {
    @ApiModelProperty(value = "科学院编号", dataType = "string", required = true)
    private String aosNo;            //科学院编号
    @ApiModelProperty(value = "国家ID", dataType = "string", required = true)
    private String countryId;         //国家ID
    @ApiModelProperty(value = "中文名", dataType = "string", required = true)
    private String aosCnname;         //中文名
    @ApiModelProperty(value = "英文名", dataType = "string", required = true)
    private String aosEnname;         //英文名
    @ApiModelProperty(value = "Home页", dataType = "string", required = true)
    private String aosHomePage;         //Home页
    @ApiModelProperty(value = "Logo地址", dataType = "string", required = true)
    private String aosLogoUrl;         //Logo地址
    @ApiModelProperty(value = "所属大洲", dataType = "string", required = true)
    private String aosContinent;        //所属大洲
}

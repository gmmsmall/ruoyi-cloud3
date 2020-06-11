package com.ruoyi.acad.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "分页", description = "分页请求类")
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    @ApiModelProperty(value = "每页行数", dataType = "int")
    private int pageSize = 10;
    @ApiModelProperty(value = "页号", dataType = "int")
    private int pageNum = 1;

    @ApiModelProperty(value = "排序字段", dataType = "String",notes = "姓名:acadName,出生日期:birthday" +
            ",国籍:nativePlace,邮箱:email,研究领域:rsfCategory,联络方式:contactMethon," +
            "已签约类型:signType,是否拉黑:isBlack,是否展示:isShow")
    private String sortField;
    @ApiModelProperty(value = "升序或降序", dataType = "String",notes = "升序:DESC,降序:ASC")
    private String sortOrder;
}

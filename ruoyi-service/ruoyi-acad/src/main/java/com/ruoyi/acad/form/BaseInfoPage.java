package com.ruoyi.acad.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 院士信息列表展示实体类
 * @Author guomiaomiao
 * @Date 2020/6/9 17:13
 * @Version 1.0
 */
@ApiModel(value = "院士信息列表展示实体类",description = "院士信息列表展示实体类")
@Data
public class BaseInfoPage implements Serializable {

    @ApiModelProperty("院士列表展示信息")
    private List<BaseInfoShowForm> content;

    @ApiModelProperty("每页显示条数")
    private Integer size;

    @ApiModelProperty("总条数")
    private Long totalElements;

    @ApiModelProperty("总页数")
    private Integer totalPages;

    @ApiModelProperty("当前页数")
    private Integer number;

}

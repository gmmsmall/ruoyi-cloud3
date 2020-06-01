package com.ruoyi.system.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("分页实体类")
public class ListResult<T> {
    @ApiModelProperty(value = "当前页数", required = true)
    public Integer pageNum;
    @ApiModelProperty(value = "总页数", required = true)
    public Long total;
    @ApiModelProperty(value = "数据", required = true)
    public List<T> rows;

//    public static ListResult list(List<T> list, Long total, QueryRequest queryRequest) {
//        ListResult listResult = new ListResult();
//        listResult.setPageNum(queryRequest.getPageNum());
//        if (total <= queryRequest.getPageSize()) {
//            listResult.setTotal(1L);
//        } else {
//            if (total % queryRequest.getPageSize() == 0){
//                listResult.setTotal(total / queryRequest.getPageSize());
//            }else {
//                listResult.setTotal((total / queryRequest.getPageSize()) + 1);
//            }
//        }
//        listResult.setRows(list);
//        return listResult;
//    }
}

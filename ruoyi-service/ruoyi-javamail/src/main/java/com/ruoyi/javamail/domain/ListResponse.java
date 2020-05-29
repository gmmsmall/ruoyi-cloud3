package com.ruoyi.javamail.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class ListResponse<T> extends PageResponse {
    List<T> list;
}

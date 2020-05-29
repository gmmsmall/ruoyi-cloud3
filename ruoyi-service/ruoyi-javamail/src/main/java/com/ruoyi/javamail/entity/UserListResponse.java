package com.ruoyi.javamail.entity;

import com.ruoyi.javamail.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class UserListResponse {
    @ApiModelProperty(value = "总数量", example = "10", required = true)
    long total;
    @ApiModelProperty(value = "当前页的数量", example = "3", required = true)
    long size;
    List<User> userList;
}

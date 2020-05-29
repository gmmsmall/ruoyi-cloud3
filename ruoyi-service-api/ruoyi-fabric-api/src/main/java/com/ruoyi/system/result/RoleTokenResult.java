package com.ruoyi.system.result;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.Token;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value = "区块链role权限对象" , description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleTokenResult {

    @ApiModelProperty(value = "令牌列表" , dataType = "string" , required = true)
    List<Token> tokenList;
    @ApiModelProperty(value = "研究领域列表" , dataType = "string" , required = true)
    List<Aos> aosList;
}

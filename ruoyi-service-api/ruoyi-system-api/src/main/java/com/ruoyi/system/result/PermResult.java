package com.ruoyi.system.result;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.Token;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PermResult {

    @ApiModelProperty(hidden = true)
    private List<Token> tokenList;

    @ApiModelProperty(hidden = true)
    private List<Aos> aosList;

    private String roleName;
}

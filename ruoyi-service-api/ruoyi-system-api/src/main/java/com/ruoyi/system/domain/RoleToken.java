package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jxd
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RoleToken对象", description = "")
public class RoleToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roleId;

    private String tokenNo;

}

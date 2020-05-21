package com.ruoyi.system.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 院士信息
 * @author xwp
 */
@Getter
@Setter
public class Academician {
    private String hash;		 //院士信息的json字符串
    private String acadNo;		 //院士编号
}

package com.ruoyi.acad.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description：科学院信息表<br/>
 * CreateTime ：2020年3月11日上午10:04:45<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acad_mst_aos")
public class MstAos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer aosId;

    private Integer countryId;

    private String aosCnname;//科学院中文名称

    private String aosEnname;//科学院英文名称

    private String aosHomepage;//科学院主页网址

    private String aosLogoUrl;//科学院LOGO图

    private Integer subAosId;//子科学院ID
    
    private String subAosCnname;//子科学院中文名称
    
    private String subAosEnname;//子科学院英文名称
    
    private String subAosHomepage;//子科学院主页
    
    private String subAosLogoUrl;//子科学院logo图
}

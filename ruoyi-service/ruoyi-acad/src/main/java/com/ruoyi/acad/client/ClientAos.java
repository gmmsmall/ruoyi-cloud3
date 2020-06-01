package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.domain.MstAos;
import com.ruoyi.acad.domain.MstCountry;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Description：院士科学院对应关系表 辅助类<br/>
 * CreateTime ：2020年3月12日下午1:11:14<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientAos extends Aos {
	
	private static final long serialVersionUID = 1L;

	private String cnName;//中文名称
	
	private String englishName;//英文名称
	
	private MstAos subAos;//子研究所
	
	private MstCountry mstCountry;//国家
	
	public ClientAos (Aos aos) {
        try {
            BeanUtils.copyProperties(this, aos);
        } catch (Exception e) {
            throw new IllegalStateException("Aos entity construction is error!");
        }
    }
}

package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.*;
import com.ruoyi.common.utils.bean.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Description：基础信息工具类<br/>
 * CreateTime ：2020年3月18日上午10:16:21<br/>
 * CreateUser：ys<br/>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientBaseInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;

	private List<Work> workList;//院士工作表
	
	private List<ClientAos> clientAosList;//院士科学院表
	
	private List<Email> emailList;//院士邮箱
	
	private AcadHomepage acadHomepage;//个人主页
	
	private Name name;
	
	private List<Education> educationList;//教育集合
	
	private MstCountry mstCountry;
	
	public ClientBaseInfo (BaseInfo baseInfo) {
        try {	
            BeanUtils.copyProperties(this, baseInfo);
        } catch (Exception e) {
            throw new IllegalStateException("BaseInfo entity construction is error!");
        }
    }
}

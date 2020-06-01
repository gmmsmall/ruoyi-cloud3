package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Email;

import java.util.List;

/**
 * @author jxd
 */
public interface IEmailService extends IService<Email> {

	/**
	 * Description:根据ID获取院士信息所有社交方式
	 * CreateTime:2020年3月18日下午1:37:13
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Email> getModelById(Integer id) throws Exception;

	/**
	 * Description:修改联系方式选项卡
	 * CreateTime:2020年3月19日下午4:16:28
	 * @return
	 * @throws Exception
	 */
	void updateModel(List<Email> emailList,Integer acadId) throws Exception;
	
	/**
	 * Description:保存联系方式
	 * CreateTime:2020年3月23日上午11:38:16
	 * @return
	 * @throws Exception
	 */
	void saveModel(List<Email> emailList,Integer acadId) throws Exception;

 }

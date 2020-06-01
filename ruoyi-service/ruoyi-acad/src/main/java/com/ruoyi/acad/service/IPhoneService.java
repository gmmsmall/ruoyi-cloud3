package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Phone;

import java.util.List;

/**
 * @author jxd
 */
public interface IPhoneService extends IService<Phone> {

	/**
	 * Description:根据id获取对应数据
	 * CreateTime:2020年3月18日下午1:40:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Phone> getModelById(Integer id) throws Exception;

	void updateModel(List<Phone> phoneList,Integer acadId) throws Exception;

	void saveModel(List<Phone> phoneList,Integer acadId) throws Exception;

}

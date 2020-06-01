package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Nationality;

import java.util.List;

/**
 * @author jxd
 */
public interface INationalityService extends IService<Nationality> {

	/**
	 * Description:保存操作
	 * CreateTime:2020年3月23日下午1:20:42
	 * @return
	 * @throws Exception
	 */
	void saveModel(List<Nationality> nationalityList, Integer acadId) throws Exception;

	/**
	 * Description:修改操作
	 * CreateTime:2020年3月23日下午1:21:13
	 * @param nationalityList
	 * @return
	 * @throws Exception
	 */
	void updateModel(List<Nationality> nationalityList, Integer acadId) throws Exception;

	/**
	 * Description:根据ID获取信息
	 * CreateTime:2020年3月23日下午1:22:25
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	List<Nationality> getModelById(Integer acadId) throws Exception;

}

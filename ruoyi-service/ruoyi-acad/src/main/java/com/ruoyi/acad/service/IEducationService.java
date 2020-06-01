package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Education;

import java.util.List;

/**
 * @author jxd
 */
public interface IEducationService extends IService<Education> {

	/**
	 * Description:根据ID获取对应信息
	 * CreateTime:2020年3月18日下午2:08:45
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	List<Education> getModelById(Integer acadId) throws Exception;

	/**
	 * Description:教育信息及联系方式修改
	 * CreateTime:2020年3月20日上午9:32:26
	 * @param eduList
	 * @return
	 * @throws Exception
	 */
	List<Education> updateModel(List<Education> eduList,Integer acadId) throws Exception;

	/**
	 * Description:保存
	 * CreateTime:2020年3月23日下午1:19:25
	 * @param educationList
	 * @return
	 * @throws Exception 
	 */
	List<Education> saveModel(List<Education> educationList, Integer acadId) throws Exception;

}

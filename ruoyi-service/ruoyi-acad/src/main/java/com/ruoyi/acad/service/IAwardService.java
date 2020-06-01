package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Award;

import java.util.List;

/**
 * @author jxd
 */
public interface IAwardService extends IService<Award> {

	/**
	 * Description:根据ID获取对应获奖信息列表
	 * CreateTime:2020年3月20日下午2:54:24
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	List<Award> getModelById(Integer acadId) throws Exception;

	/**
	 * Description:保存获奖荣誉信息
	 * CreateTime:2020年3月23日下午12:00:51
	 * @param awardList
	 * @return
	 * @throws Exception
	 */
	List<Award> saveModel(List<Award> awardList,Integer acadId) throws Exception;
	
	/**
	 * Description:修改荣誉信息
	 * CreateTime:2020年3月23日下午12:01:25
	 * @param awardList
	 * @return
	 * @throws Exception
	 */
	List<Award> updateModel(List<Award> awardList,Integer acadId) throws Exception;

}

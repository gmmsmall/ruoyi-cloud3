package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Patent;

import java.util.List;

/**
 * @author jxd
 */
public interface IPatentService extends IService<Patent> {

	/**
	 * Description:论文保存
	 * CreateTime:2020年3月23日下午1:27:32
	 * @param patentList
	 * @return
	 * @throws Exception
	 */
	List<Patent> saveModel(List<Patent> patentList, Integer acadId) throws Exception;
	
	/**
	 * Description:论文修改
	 * CreateTime:2020年3月23日下午1:27:39
	 * @param patentList
	 * @return
	 * @throws Exception
	 */
	List<Patent> updateModel(List<Patent> patentList, Integer acadId) throws Exception;
	
	/**
	 * Description:根据院士id获取论文信息
	 * CreateTime:2020年3月23日下午1:27:46
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	List<Patent> getModelById(Integer acadId) throws Exception;

}

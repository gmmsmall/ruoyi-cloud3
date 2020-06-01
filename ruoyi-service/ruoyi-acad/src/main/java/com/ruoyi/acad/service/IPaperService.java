package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Paper;

import java.util.List;

/**
 * @author jxd
 */
public interface IPaperService extends IService<Paper> {

	/**
	 * Description:保存操作
	 * CreateTime:2020年3月23日下午1:20:42
	 * @param paperList
	 * @return
	 * @throws Exception
	 */
	List<Paper> saveModel(List<Paper> paperList, Integer acadId) throws Exception;
	
	/**
	 * Description:修改操作
	 * CreateTime:2020年3月23日下午1:21:13
	 * @param paperList
	 * @return
	 * @throws Exception
	 */
	List<Paper> updateModel(List<Paper> paperList, Integer acadId) throws Exception;
	
	/**
	 * Description:根据ID获取信息
	 * CreateTime:2020年3月23日下午1:22:25
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	 List<Paper> getModelByAcadId(Integer acadId) throws Exception;

}

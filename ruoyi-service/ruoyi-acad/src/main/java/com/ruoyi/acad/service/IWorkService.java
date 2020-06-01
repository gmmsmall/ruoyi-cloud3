package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Work;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
public interface IWorkService extends IService<Work> {

	/**
	 * Description:根据id获取信息
	 * CreateTime:2020年3月18日下午1:27:29
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Work> getModelById(@NotBlank(message = "{required}") Integer id) throws Exception;

	/**
	 * Description:修改工作内容
	 * CreateTime:2020年3月19日上午9:35:34
	 * @param list
	 * @return
	 * @throws Exception
	 */
	List<Work> updateModel(List<Work> list,Integer acadId) throws Exception;
 
	/**
	 * Description:修改修改操作
	 * CreateTime:2020年3月23日上午11:48:16
	 * @param workList
	 * @throws Exception
	 */
	List<Work> saveModel(List<Work> workList, Integer acadId) throws Exception;

}

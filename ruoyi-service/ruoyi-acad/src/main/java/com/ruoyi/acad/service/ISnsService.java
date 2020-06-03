package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Sns;

import java.util.List;

/**
 * @author jxd
 */
public interface ISnsService extends IService<Sns> {


	/**
	 * Description:根据ID获取对应实体信息
	 * CreateTime:2020年3月18日下午1:42:17
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Sns> getModelById(Integer id) throws Exception;

	public void updateModel(List<Sns> snsList, Integer acadId) throws Exception;

	public void saveModel(List<Sns> snsList,Integer acadId) throws Exception;
}

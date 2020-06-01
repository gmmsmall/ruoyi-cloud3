package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Aos;

import java.util.List;

/**
 * @author jxd
 */
public interface IAosService extends IService<Aos> {

	/**
	 * Description:保存院士获奖
	 * CreateTime:2020年3月12日下午5:29:30
	 * @return
	 * @throws Exception
	 */
	public void saveModel(List<Aos> aosList,Integer acadId) throws Exception ;

	
	/**
	 * Description:根据ID获取对应实体数据
	 * CreateTime:2020年3月13日上午9:20:58
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	public List<Aos> getModelById(Integer acadId) throws Exception;

	/**
	 * Description:修改实体
	 * CreateTime:2020年3月20日下午2:12:00
	 * @throws Exception
	 */
	public void updateModel(List<Aos> aosList,Integer acadId) throws Exception;


}

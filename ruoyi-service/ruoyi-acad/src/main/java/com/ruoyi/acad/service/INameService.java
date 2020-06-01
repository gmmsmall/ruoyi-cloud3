package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Name;

/**
 * @author jxd
 */
public interface INameService extends IService<Name> {

	Name getModelById(Integer id) throws Exception;

	void updateModel(Name name) throws Exception;

	void saveModel(Name name) throws Exception;

}

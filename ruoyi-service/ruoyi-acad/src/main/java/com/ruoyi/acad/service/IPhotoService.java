package com.ruoyi.acad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.acad.domain.Photo;

import java.util.List;

/**
 * @author jxd
 */
public interface IPhotoService extends IService<Photo> {

	/**
	 * Description:根据id获取对应数据
	 * CreateTime:2020年3月18日下午1:40:33
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Photo> getModelById(Integer id) throws Exception;

	void saveModel(Photo photo) throws Exception;

	void deleteModel(long photoId) throws Exception;

	 void initGender();
}

package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.dao.PhotoMapper;
import com.ruoyi.acad.domain.Photo;
import com.ruoyi.acad.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jxd
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {

	@Autowired
	private PhotoMapper photoMapper;

	/**
	 * 根据ID获取信息
	 */
	@Override
	public List<Photo> getModelById(Integer id) throws Exception {
		
		LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAcadId,id);
		queryWrapper.eq(Photo::getDelFlag,false);
		
		List<Photo> phoneList = photoMapper.selectList(queryWrapper);
		return phoneList;
	}

	@Override
	public void saveModel(Photo photo) throws Exception {
		photo.setAiDatetime(LocalDate.now());
		this.photoMapper.insert(photo);
	}

	@Override
	public void deleteModel(long photoId) throws Exception {

		Photo photo = new Photo();
		photo.setPhotoId(photoId);
		photo.setDelFlag(true);
		this.photoMapper.update(photo,new QueryWrapper<Photo>().eq("photo_id",photoId));
	}

}

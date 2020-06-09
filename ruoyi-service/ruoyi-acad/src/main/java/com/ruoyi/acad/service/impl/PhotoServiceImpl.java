package com.ruoyi.acad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.PhotoMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Photo;
import com.ruoyi.acad.form.PhotoForm;
import com.ruoyi.acad.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jxd
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {

	@Autowired
	private PhotoMapper photoMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;

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
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(photo.getAcadId()));
		ClientAcad clientAcad = optionalClientAcad.get();
		List<PhotoForm> photoList = clientAcad.getPhotoList();
		PhotoForm photoForm = new PhotoForm();
		BeanUtil.copyProperties(photo,photoForm);
		if(CollUtil.isNotEmpty(photoList)){
			photoList.add(photoForm);
		}else{
			photoList = new ArrayList<PhotoForm>();
			photoList.add(photoForm);
		}
		clientAcad.setPhotoList(photoList);
		elasticClientAcadRepository.save(clientAcad);
	}

	@Override
	public void deleteModel(long photoId) throws Exception {

		Photo photoTemp = this.photoMapper.selectById(photoId);
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(photoTemp.getAcadId()));
		ClientAcad clientAcad = optionalClientAcad.get();
		List<PhotoForm> photoList = clientAcad.getPhotoList();
		PhotoForm photoForm = new PhotoForm();
		BeanUtil.copyProperties(photoTemp,photoForm);
		if(CollUtil.isNotEmpty(photoList)){
			photoList.remove(photoForm);
		}
		clientAcad.setPhotoList(photoList);
		elasticClientAcadRepository.save(clientAcad);

		Photo photo = new Photo();
		photo.setPhotoId(photoId);
		photo.setDelFlag(true);
		this.photoMapper.update(photo,new QueryWrapper<Photo>().eq("photo_id",photoId));

	}

}

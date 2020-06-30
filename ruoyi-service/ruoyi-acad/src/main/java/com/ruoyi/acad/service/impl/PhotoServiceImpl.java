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
import com.ruoyi.acad.utils.AipFaceUtil;
import com.ruoyi.common.utils.StringUtils;
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

	@Autowired
	private AipFaceUtil aipFaceUtil;

	/**
	 * 根据ID获取信息
	 */
	@Override
	public List<Photo> getModelById(Integer id) throws Exception {
		return this.photoMapper.selectList(new QueryWrapper<Photo>().eq("acad_id",id).eq("del_flag",1));
	}

	@Override
	public void saveModel(Photo photo) throws Exception {
		photo.setAiDatetime(LocalDate.now());
		if(photo != null && StringUtils.isNotEmpty(photo.getPhotoUrl())){
			//识别照片性别
			String sex = aipFaceUtil.getSexByImage(photo.getPhotoUrl());
			if(StringUtils.isNotEmpty(sex)){
				if(sex.equals("male")){//男
					photo.setAiGender(1);
				}else if(sex.equals("female")){//女
					photo.setAiGender(2);
				}else{
					photo.setAiGender(3);//未知
				}
			}
		}
		photo.setDelFlag(true);
		this.photoMapper.insert(photo);
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(photo.getAcadId()));
		ClientAcad clientAcad = optionalClientAcad.get();
		List<PhotoForm> photoList = this.photoMapper.getPhotoFormList(photo.getAcadId());
		clientAcad.setPhotoList(photoList);
		elasticClientAcadRepository.save(clientAcad);
	}

	/**
	 * 保存院士照片列表
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void saveModelList(List<Photo> list,Integer acadId) throws Exception {
		if(CollUtil.isNotEmpty(list)){
			this.photoMapper.deletePhotoByAcadId(acadId);
			LocalDate now = LocalDate.now();
			for(Photo photo : list){
				photo.setAiDatetime(now);
				if(photo != null && StringUtils.isNotEmpty(photo.getPhotoUrl())){
					//识别照片性别
					String sex = aipFaceUtil.getSexByImage(photo.getPhotoUrl());
					if(StringUtils.isNotEmpty(sex)){
						if(sex.equals("male")){//男
							photo.setAiGender(1);
						}else if(sex.equals("female")){//女
							photo.setAiGender(2);
						}else{
							photo.setAiGender(3);//未知
						}
					}
				}
				photo.setDelFlag(true);
				this.photoMapper.insert(photo);
			}
			Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
			ClientAcad clientAcad = optionalClientAcad.get();
			List<PhotoForm> photoList = this.photoMapper.getPhotoFormList(Long.valueOf(acadId));
			clientAcad.setPhotoList(photoList);
			elasticClientAcadRepository.save(clientAcad);
		}

	}

	@Override
	public void deleteModel(long photoId) throws Exception {

		Photo photoTemp = this.photoMapper.selectOne(new QueryWrapper<Photo>().eq("photo_id",photoId).eq("del_flag",1));
		Photo photo = new Photo();
		photo.setPhotoId(photoId);
		photo.setDelFlag(false);
		this.photoMapper.update(photo,new QueryWrapper<Photo>().eq("photo_id",photoId));

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(photoTemp.getAcadId()));
		ClientAcad clientAcad = optionalClientAcad.get();
		List<PhotoForm> photoList = this.photoMapper.getPhotoFormList(photoTemp.getAcadId());
		clientAcad.setPhotoList(photoList);
		clientAcad.setPhotoList(photoList);
		elasticClientAcadRepository.save(clientAcad);

	}

	/**
	 * 初始化照片性别
	 */
	@Override
	public void initGender() {
		try{
			List<Photo> list = this.photoMapper.selectList(new QueryWrapper<Photo>().isNull("ai_gender").orderByAsc("photo_id"));
			if(CollUtil.isNotEmpty(list)){
				for(Photo photo : list){
					if(StringUtils.isNotEmpty(photo.getPhotoUrl())){
						//识别照片性别
						String sex = aipFaceUtil.getSexByImage("http://"+photo.getPhotoUrl());
						if(StringUtils.isNotEmpty(sex)){
							if(sex.equals("male")){//男
								photo.setAiGender(1);
							}else if(sex.equals("female")){//女
								photo.setAiGender(2);
							}else{
								photo.setAiGender(3);//未知
							}
						}
						photo.setAiDatetime(LocalDate.now());
						this.photoMapper.update(photo,new QueryWrapper<Photo>().eq("photo_id",photo.getPhotoId()));
						Thread.sleep(1000); //1000 毫秒，也就是1秒.
					}
				}
			}
		}catch (Exception e){

		}
	}

}

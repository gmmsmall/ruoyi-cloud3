package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.EducationMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Education;
import com.ruoyi.acad.service.IEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Description：邮件服务层实现<br/>
 * CreateTime ：2020年3月20日上午9:32:45<br/>
 * CreateUser：ys<br/>
 */
@Service
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education> implements IEducationService {

	@Autowired
	private EducationMapper educationMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据ID获取对应信息
	 */
	@Override
	public List<Education> getModelById(Integer acadId) throws Exception {
		
		LambdaQueryWrapper<Education> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Education::getAcadId,acadId);
        
        List<Education> result = educationMapper.selectList(queryWrapper);
		return result;
	}

	/**
	 * 修改教育经历
	 */
	@Override
	public List<Education> updateModel(List<Education> eduList,Integer acadId) throws Exception {

		LambdaQueryWrapper<Education> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Education::getAcadId,acadId);
		educationMapper.delete(queryWrapper);
		//保存修改的教育信息
		eduList.stream().forEach(x ->{
			x.setAcadId(acadId);
			educationMapper.insert(x);
		});

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setEducationList(eduList);
		elasticClientAcadRepository.save(clientAcad);

		return eduList;
	}

	/**
	 * 保存操作
	 */
	@Override
	public List<Education> saveModel(List<Education> educationList,Integer acadId) throws Exception {
		
		//保存修改的教育信息
		educationList.stream().forEach(x ->{
			x.setAcadId(acadId);
			this.save(x);
		});

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setEducationList(educationList);
		elasticClientAcadRepository.save(clientAcad);

		return educationList;
	}

}

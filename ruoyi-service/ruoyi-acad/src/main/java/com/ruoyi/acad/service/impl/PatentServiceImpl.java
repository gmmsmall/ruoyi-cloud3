package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.PatentMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Patent;
import com.ruoyi.acad.service.IPatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author jxd
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PatentServiceImpl extends ServiceImpl<PatentMapper, Patent> implements IPatentService {

	@Autowired
	private PatentMapper patentMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 保存论文列表
	 */
	@Override
	public List<Patent> saveModel(List<Patent> patentList,Integer acadId) throws Exception {
		
		if (patentList != null && patentList.size() > 0) {
			patentList.stream().forEach(x ->{
				x.setAcadId(acadId);
				this.save(x);
			});
			Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
			ClientAcad clientAcad = optionalClientAcad.get();
			clientAcad.setPatentList(patentList);
			elasticClientAcadRepository.save(clientAcad);
		}
		
		return patentList;
	}

	/**
	 * 修改保存论文列表
	 */
	@Override
	public List<Patent> updateModel(List<Patent> patentList,Integer acadId) throws Exception {
		
		LambdaQueryWrapper<Patent> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Patent::getAcadId,acadId);
		patentMapper.delete(queryWrapper);
		patentList.stream().forEach(x ->{
			x.setAcadId(acadId);
			this.patentMapper.insert(x);
		});
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setPatentList(patentList);
		elasticClientAcadRepository.save(clientAcad);

		return patentList;
	}

	/**
	 * 根据院士ID获取沦为列表
	 */
	@Override
	public List<Patent> getModelById(Integer acadId) throws Exception {
		
		List<Patent> list = patentMapper.selectList(new LambdaQueryWrapper<Patent>().eq(Patent::getAcadId,acadId).orderByDesc(Patent::getGetTime));
		return list;
	}

}

package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.SnsMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Sns;
import com.ruoyi.acad.service.ISnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jxd
 */
@Service
public class SnsServiceImpl extends ServiceImpl<SnsMapper, Sns> implements ISnsService {

	@Autowired
	private SnsMapper snsMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据ID获取信息
	 */
	@Override
	public Sns getModelById(Integer id) throws Exception {
		
		LambdaQueryWrapper<Sns> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Sns::getAcadId,id);
		
		Sns sns = snsMapper.selectOne(queryWrapper);
		return sns;
	}

	@Override
	public void updateModel(List<Sns> snsList,Integer acadId) throws Exception {


		LambdaQueryWrapper<Sns> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Sns::getAcadId,acadId);
		snsMapper.delete(queryWrapper);
		for (Sns sns:snsList) {
			sns.setAcadId(acadId);
			snsMapper.insert(sns);
		}
		ClientAcad acad = new ClientAcad();
		acad.setAcadId(acadId);
		acad.setSnsList(snsList);
		elasticClientAcadRepository.save(acad);
	}

	@Override
	public void saveModel(List<Sns> snsList,Integer acadId) throws Exception {

		for (Sns sns:snsList) {
			sns.setAcadId(acadId);
			snsMapper.insert(sns);
		}
		ClientAcad acad = new ClientAcad();
		acad.setAcadId(acadId);
		acad.setSnsList(snsList);
		elasticClientAcadRepository.save(acad);
	}

}

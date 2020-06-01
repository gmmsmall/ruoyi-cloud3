package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.AosMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.service.IAosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description：创建院士科学院对应关系serviceImpl<br/>
 * CreateTime ：2020年3月13日上午9:22:02<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AosServiceImpl extends ServiceImpl<AosMapper, Aos> implements IAosService {

	@Autowired
	private AosMapper aosMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据ID获取对应数据
	 */
	@Override
	public List<Aos> getModelById(Integer acadId) throws Exception {
		
		//查询所有院士当选院士信息
		List<Aos> aosList = baseMapper.selectList(new LambdaQueryWrapper<Aos>().eq(Aos::getAcadId, acadId));

		return aosList;
	}

	/**
	 * 修改院士授衔信息列表
	 */
	@Override
	public void updateModel(List<Aos> aosList,Integer acadId) throws Exception {
		
		LambdaQueryWrapper<Aos> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Aos::getAcadId,acadId);
		aosMapper.delete(queryWrapper);
		aosList.stream().forEach(x ->{
			x.setAcadId(acadId);
			aosMapper.insert(x);
		});

		ClientAcad clientAcad = new ClientAcad();
		clientAcad.setAosList(aosList);
		elasticClientAcadRepository.save(clientAcad);
	}

	/**
	 * 保存实体
	 */
	@Override
	public void saveModel(List<Aos> aosList,Integer acadId) throws Exception {
		
		if (aosList != null && aosList.size() > 0) {
			aosList.stream().forEach(x ->{
				x.setAcadId(acadId);
				this.save(x);
			});
			ClientAcad clientAcad = new ClientAcad();
			clientAcad.setAosList(aosList);
			elasticClientAcadRepository.save(clientAcad);
		}
	}

}

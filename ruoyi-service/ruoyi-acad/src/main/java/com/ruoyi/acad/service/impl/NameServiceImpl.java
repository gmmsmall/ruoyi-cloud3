package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.NameMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Name;
import com.ruoyi.acad.service.INameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jxd
 */
@Service
public class NameServiceImpl extends ServiceImpl<NameMapper, Name> implements INameService {

	@Autowired
	private NameMapper nameMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据ID获取信息
	 */
	@Override
	public Name getModelById(Integer id) throws Exception {
		
		LambdaQueryWrapper<Name> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Name::getAcadId,id);

		Name name = nameMapper.selectOne(queryWrapper);
		return name;
	}

	@Override
	public void updateModel(Name name) throws Exception {

		nameMapper.updateById(name);
		ClientAcad acad = new ClientAcad();
		acad.setName(name);
		elasticClientAcadRepository.save(acad);
	}

	@Override
	public void saveModel(Name name) throws Exception {

		nameMapper.insert(name);
		ClientAcad acad = new ClientAcad();
		acad.setAcadId(name.getAcadId());
		acad.setName(name);
		elasticClientAcadRepository.save(acad);
	}

}

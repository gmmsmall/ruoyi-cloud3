package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.PhoneMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Phone;
import com.ruoyi.acad.service.IPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jxd
 */
@Service
public class PhoneServiceImpl extends ServiceImpl<PhoneMapper, Phone> implements IPhoneService {

	@Autowired
	private PhoneMapper phoneMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据ID获取信息
	 */
	@Override
	public List<Phone> getModelById(Integer id) throws Exception {
		
		LambdaQueryWrapper<Phone> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Phone::getAcadId,id);
		
		List<Phone> phoneList = phoneMapper.selectList(queryWrapper);
		return phoneList;
	}

	@Override
	public void updateModel(List<Phone> phoneList,Integer acadId) throws Exception {

		LambdaQueryWrapper<Phone> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Phone::getAcadId,acadId);
		phoneMapper.delete(queryWrapper);
		for (Phone phone:phoneList) {
			phone.setAcadId(acadId);
			phoneMapper.insert(phone);
		}
		ClientAcad acad = new ClientAcad();
		/*acad.setAcadId(acadId);*/
		acad.setPhoneList(phoneList);
		elasticClientAcadRepository.save(acad);
	}

	@Override
	public void saveModel(List<Phone> phoneList,Integer acadId) throws Exception {

		for (Phone phone:phoneList) {
			phone.setAcadId(acadId);
			phoneMapper.insert(phone);
		}
		ClientAcad acad = new ClientAcad();
		/*acad.setAcadId(acadId);*/
		acad.setPhoneList(phoneList);
		elasticClientAcadRepository.save(acad);
	}

}

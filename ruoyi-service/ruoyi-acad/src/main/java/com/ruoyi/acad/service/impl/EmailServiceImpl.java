package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.EmailMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Email;
import com.ruoyi.acad.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Description：创建邮箱实现层<br/>
 * CreateTime ：2020年4月1日上午9:31:59<br/>
 * CreateUser：ys<br/>
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements IEmailService {

	@Autowired
	private EmailMapper emailMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;


	@Override
	public List<Email> getModelById(Integer id) throws Exception {

		LambdaQueryWrapper<Email> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Email::getAcadId,id);

		List<Email> emailList = emailMapper.selectList(queryWrapper);
		return emailList;
	}

	@Override
	public void updateModel(List<Email> emailList,Integer acadId) throws Exception {

		LambdaQueryWrapper<Email> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Email::getAcadId,acadId);
		emailMapper.delete(queryWrapper);
		for (Email email:emailList) {
			email.setAcadId(acadId);
			emailMapper.insert(email);
		}

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setEmailList(emailList);
		elasticClientAcadRepository.save(clientAcad);
	}

	@Override
	public void saveModel(List<Email> emailList,Integer acadId) throws Exception {

		for (Email email:emailList) {
			if (email.getAcadId() != null) {
				email.setAcadId(acadId);
				emailMapper.insert(email);
			}
		}
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setEmailList(emailList);
		elasticClientAcadRepository.save(clientAcad);
	}
}

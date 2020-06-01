package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.NationalityMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Nationality;
import com.ruoyi.acad.service.INationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jxd
 */
@Service
public class NationalityServiceImpl extends ServiceImpl<NationalityMapper, Nationality> implements INationalityService {

	@Autowired
	private NationalityMapper nationalityMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;

	/**
	 * 新增国籍信息
	 */
	@Override
	public void saveModel(List<Nationality> nationalityList, Integer acadId) throws Exception {

		if (nationalityList != null && nationalityList.size() > 0) {
			nationalityList.stream().forEach(x ->{
				x.setAcadId(acadId);
				this.save(x);
			});
			ClientAcad acad = new ClientAcad();
			acad.setAcadId(acadId);
			acad.setNationalityList(nationalityList);
			elasticClientAcadRepository.save(acad);
		}
	}

	/**
	 * 修改国籍信息
	 */
	@Override
	public void updateModel(List<Nationality> nationalityList, Integer acadId) throws Exception {

		LambdaQueryWrapper<Nationality> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Nationality::getAcadId,acadId);
		nationalityMapper.delete(queryWrapper);
		nationalityList.stream().forEach(x ->{
			x.setAcadId(acadId);
			this.nationalityMapper.insert(x);
		});
		ClientAcad acad = new ClientAcad();
		acad.setAcadId(acadId);
		acad.setNationalityList(nationalityList);
		elasticClientAcadRepository.save(acad);
	}

	/**
	 * 获取国籍列表
	 */
	@Override
	public List<Nationality> getModelById(Integer acadId) throws Exception {

		List<Nationality> list = nationalityMapper.selectList(new LambdaQueryWrapper<Nationality>().eq(Nationality::getAcadId,acadId));
		return list;
	}
}

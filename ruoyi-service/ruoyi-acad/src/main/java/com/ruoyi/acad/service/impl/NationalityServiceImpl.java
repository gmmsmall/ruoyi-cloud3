package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.NationalityMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.Nationality;
import com.ruoyi.acad.service.IBaseInfoService;
import com.ruoyi.acad.service.INationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author jxd
 */
@Service
public class NationalityServiceImpl extends ServiceImpl<NationalityMapper, Nationality> implements INationalityService {

	@Autowired
	private NationalityMapper nationalityMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;

	@Autowired
	private IBaseInfoService baseInfoService;

	/**
	 * 新增国籍信息
	 */
	@Override
	public void saveModel(List<Nationality> nationalityList, Integer acadId) throws Exception {

		String nationStr = "";//多个国籍拼接成的字符串
		if (nationalityList != null && nationalityList.size() > 0) {
			nationalityList.stream().forEach(x ->{
				x.setAcadId(acadId);
				this.save(x);
			});
			for(int i = 0 ; i < nationalityList.size();i++){
				if(i == 0){
					nationStr = nationalityList.get(i).getCountryName();
				}else{
					nationStr = nationStr + "," + nationalityList.get(i).getCountryName();
				}
			}

			Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
			ClientAcad clientAcad = optionalClientAcad.get();
			//修改基础信息中的国籍
			BaseInfo baseInfo = clientAcad.getBaseInfo();
			baseInfo.setNationPlace(nationStr);
			clientAcad.setBaseInfo(baseInfo);
			clientAcad.setNationalityList(nationalityList);
			elasticClientAcadRepository.save(clientAcad);
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
		String nationStr = "";//多个国籍拼接成的字符串
		if(CollUtil.isNotEmpty(nationalityList)){
			for(int i = 0 ; i < nationalityList.size();i++){
				if(i == 0){
					nationStr = nationalityList.get(i).getCountryName();
				}else{
					nationStr = nationStr + "," + nationalityList.get(i).getCountryName();
				}
			}
		}

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		//修改基础信息中的国籍
		BaseInfo baseInfo = clientAcad.getBaseInfo();
		baseInfo.setNationPlace(nationStr);
		clientAcad.setBaseInfo(baseInfo);
		clientAcad.setNationalityList(nationalityList);
		elasticClientAcadRepository.save(clientAcad);
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

package com.ruoyi.acad.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.client.ClientBaseInfo;
import com.ruoyi.acad.dao.AosMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.service.IAosService;
import com.ruoyi.acad.service.IBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
	 * 院士基础信息service
	 */
	@Autowired
	private IBaseInfoService baseInfoService;
	
	/**
	 * 根据ID获取对应数据
	 */
	@Override
	public List<Aos> getModelById(Integer acadId) throws Exception {
		
		//查询所有院士当选院士信息
		List<Aos> aosList = baseMapper.selectList(new LambdaQueryWrapper<Aos>().eq(Aos::getAcadId, acadId)
				.notIn(Aos::getAosNo,"1592550962574268722"));//暂无科学院不显示

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
			x.setAosId(Long.valueOf(x.getAosNo()));
			aosMapper.insert(x);
		});

		String aosStr = "";//多个科学院拼接成的字符串
		if(CollUtil.isNotEmpty(aosList)){
			for(int i = 0 ; i < aosList.size();i++){
				if(i == 0){
					aosStr = aosList.get(i).getAosName();
				}else{
					aosStr = aosStr + "," + aosList.get(i).getAosName();
				}
			}
		}
		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		//修改基础信息中的科学院
		BaseInfo baseInfo = clientAcad.getBaseInfo();
		baseInfo.setAosName(aosStr);
		clientAcad.setBaseInfo(baseInfo);
		clientAcad.setAosList(aosList);
		elasticClientAcadRepository.save(clientAcad);
	}

	/**
	 * 保存实体
	 */
	@Override
	public void saveModel(List<Aos> aosList,Integer acadId) throws Exception {

		LambdaQueryWrapper<Aos> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Aos::getAcadId,acadId);
		aosMapper.delete(queryWrapper);
		
		if (aosList != null && aosList.size() > 0) {
			aosList.stream().forEach(x ->{
				x.setAcadId(acadId);
				x.setAosId(Long.valueOf(x.getAosNo()));
				this.save(x);
			});
			String aosStr = "";//多个科学院拼接成的字符串
			for(int i = 0 ; i < aosList.size();i++){
				if(i == 0){
					aosStr = aosList.get(i).getAosName();
				}else{
					aosStr = aosStr + "," + aosList.get(i).getAosName();
				}
			}

			Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
			ClientAcad clientAcad = optionalClientAcad.get();
			//修改基础信息中的科学院
			BaseInfo baseInfo = clientAcad.getBaseInfo();
			baseInfo.setAosName(aosStr);
			clientAcad.setAcadId(String.valueOf(acadId));
			clientAcad.setBaseInfo(baseInfo);
			clientAcad.setAosList(aosList);
			elasticClientAcadRepository.save(clientAcad);
		}
	}

}

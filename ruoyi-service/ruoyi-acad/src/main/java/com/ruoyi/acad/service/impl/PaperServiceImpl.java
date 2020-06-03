package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.PaperMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Paper;
import com.ruoyi.acad.service.IPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author jxd
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements IPaperService {

	@Autowired
	private PaperMapper paperMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 保存操作
	 */
	@Override
	public List<Paper> saveModel(List<Paper> paperList,Integer acadId) throws Exception {
		
		if (paperList != null && paperList.size() > 0) {
			paperList.stream().forEach(x ->{
				x.setAcadId(acadId);
				this.save(x);
			});
			Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
			ClientAcad clientAcad = optionalClientAcad.get();
			clientAcad.setPaperList(paperList);
			elasticClientAcadRepository.save(clientAcad);
		}
		
		return paperList;
	}

	/**
	 * 修改操作论文
	 */
	@Override
	public List<Paper> updateModel(List<Paper> paperList,Integer acadId) throws Exception {
		

		LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Paper::getAcadId,acadId);
		paperMapper.delete(queryWrapper);
		paperList.stream().forEach(x ->{
			x.setAcadId(acadId);
			this.paperMapper.insert(x);
		});

		Optional<ClientAcad> optionalClientAcad = this.elasticClientAcadRepository.findById(String.valueOf(acadId));
		ClientAcad clientAcad = optionalClientAcad.get();
		clientAcad.setPaperList(paperList);
		elasticClientAcadRepository.save(clientAcad);
		return paperList;
	}

	/**
	 * 根据ID获取对应信息操作
	 */
	@Override
	public List<Paper> getModelByAcadId(Integer acadId) throws Exception {
		
		List<Paper> paperList = paperMapper.selectList(new LambdaQueryWrapper<Paper>().eq(Paper::getAcadId, acadId));
		
		return paperList;
	}

}

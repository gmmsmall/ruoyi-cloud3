package com.ruoyi.acad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.acad.client.ClientAcad;
import com.ruoyi.acad.dao.WorkMapper;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.Work;
import com.ruoyi.acad.service.IWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {

	@Autowired
	private WorkMapper workMapper;

	@Autowired
	private ElasticClientAcadRepository elasticClientAcadRepository;
	
	/**
	 * 根据院士ID获取全部工作内容信息
	 */
	@Override
	public List<Work> getModelById(@NotBlank(message = "{required}") Integer id) throws Exception {
		
		List<Work> workList = workMapper.selectList(new LambdaQueryWrapper<Work>().eq(Work::getAcadId, id));
		return workList;
	}

	/**
	 * 修改工作内容
	 */
	@Override
	public List<Work> updateModel(List<Work> workList,Integer acadId) throws Exception {
		
		LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Work::getAcadId,acadId);
		workMapper.delete(queryWrapper);
		workList.stream().forEach(x->{
			x.setAcadId(acadId);
			workMapper.insert(x);
		});
		ClientAcad acad = new ClientAcad();
		acad.setAcadId(acadId);
		acad.setWorkList(workList);
		elasticClientAcadRepository.save(acad);

		return workList;
	}

	/**
	 * 修改操作
	 */
	@Override
	public List<Work> saveModel(List<Work> workList,Integer acadId) throws Exception {
		
		if (workList != null && workList.size() > 0) {
			workList.stream().forEach(x->{
				x.setAcadId(acadId);
				this.save(x);
			});
			ClientAcad acad = new ClientAcad();
			acad.setAcadId(acadId);
			acad.setWorkList(workList);
			elasticClientAcadRepository.save(acad);
		}
		return workList;
	}
}

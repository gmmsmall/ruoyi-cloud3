package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.Work;
import com.ruoyi.acad.service.IWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/work")
public class WorkController{

	@Autowired
	private IWorkService workService;
	
	/**
	 * Description:根据院士id获取对应院士工作信息
	 * CreateTime:2020年3月19日上午9:28:08
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	public List<Work> getModelById(Integer acadId) throws Exception {
		
		List<Work> workList = workService.getModelById(acadId);
		return workList;
	}
	
	/**
	 * Description:修改院士工作内容
	 * CreateTime:2020年3月19日上午9:34:50
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	public ResponseResult updateModel(@RequestBody List<Work> workList,@NotBlank Integer acadId) throws Exception {
		
		workService.updateModel(workList,acadId);
		return new ResponseResult(true, 200, "修改成功");
	}

	/**
	 * Description:保存操作
	 * CreateTime:2020年3月23日上午11:01:14
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	public ResponseResult saveModel(@RequestBody List<Work> workList, @NotBlank Integer acadId) throws Exception {

		workService.saveModel(workList,acadId);
		return new ResponseResult(true, 200, "保存成功");
	}
}

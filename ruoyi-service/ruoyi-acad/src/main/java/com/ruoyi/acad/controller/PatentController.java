package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Patent;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：专利发明信息控制层<br/>
 * CreateTime ：2020年3月31日上午11:13:54<br/>
 * CreateUser：ys<br/>
 */
@RestController
@RequestMapping("/patent")
public class PatentController {

	@Autowired
	private IPatentService patentService;
	
	/**
	 * Description:保存专利信息
	 * CreateTime:2020年3月31日上午11:15:02
	 * @param patentList
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	public ResponseResult saveModel(@RequestBody List<Patent> patentList,@NotBlank Integer acadId) throws Exception {
		
		patentService.saveModel(patentList, acadId);
		return new ResponseResult(true, 200, "保存成功");
	}

	/**
	 * Description:修改专利信息
	 * CreateTime:2020年3月31日上午11:15:02
	 * @param patentList
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	public ResponseResult updateModel(@RequestBody List<Patent> patentList,@NotBlank Integer acadId) throws Exception {

		patentService.updateModel(patentList, acadId);
		return new ResponseResult(true, 200, "修改成功");
	}

	/**
	 * Description:获取专利信息
	 * CreateTime:2020年3月31日上午11:15:02
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	public List<Patent> getModelById(@NotBlank Integer acadId) throws Exception {

		List<Patent> patentList = patentService.getModelById(acadId);
		return patentList;
	}
}

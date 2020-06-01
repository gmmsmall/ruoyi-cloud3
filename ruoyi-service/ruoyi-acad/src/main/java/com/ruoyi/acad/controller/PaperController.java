package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Paper;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：创建院士论文控制层<br/>
 * CreateTime ：2020年3月31日上午11:12:13<br/>
 * CreateUser：ys<br/>
 */
@RestController
@RequestMapping("/paper")
public class PaperController {

	@Autowired
	private IPaperService paperService;
	
	/**
	 * Description:保存论文信息
	 * CreateTime:2020年3月31日上午11:08:02
	 * @param paperList
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	public ResponseResult saveModel(@RequestBody List<Paper> paperList, @NotBlank Integer acadId) throws Exception {
		
		paperService.saveModel(paperList, acadId);
		return new ResponseResult(true, 200, "保存成功");
	}

	/**
	 * Description:保存论文信息
	 * CreateTime:2020年3月31日上午11:08:02
	 * @param paperList
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	public ResponseResult updateModel(@RequestBody List<Paper> paperList, @NotBlank Integer acadId) throws Exception {

		paperService.updateModel(paperList, acadId);
		return new ResponseResult(true, 200, "修改成功");
	}
	
	/**
	 * Description:根据院士ID获取院士论文信息
	 * CreateTime:2020年4月10日下午3:34:38
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	public List<Paper> getModelById(Integer acadId) throws Exception {

		List<Paper> paperList = paperService.getModelByAcadId(acadId);
		return paperList;
	}
}

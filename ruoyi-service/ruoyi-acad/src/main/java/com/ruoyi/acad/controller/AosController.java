package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IAosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description：院士科学院对应关系控制层<br/>
 * CreateTime ：2020年3月17日上午9:12:05<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/aos")
public class AosController{

	@Autowired
	private IAosService aosService;

	/**
	 * Description:根据ID获取对应数据
	 * CreateTime:2020年3月17日上午9:15:00
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	public List<Aos> getModelById(Integer acadId) throws Exception {
		
		List<Aos> aosList = aosService.getModelById(acadId);
		return aosList;
	}
	
	/**
	 * Description：修改授衔履历
	 * CreateTime:2020年3月20日下午1:50:23
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	public ResponseResult updateModel(List<Aos> aosList,Integer acadId) throws Exception {
		
		aosService.updateModel(aosList,acadId);
		return new ResponseResult(true, 200, "修改成功");
	}

	/**
	 * Description：新增授衔履历
	 * CreateTime:2020年3月20日下午1:50:23
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	public ResponseResult saveModel(List<Aos> aosList,Integer acadId) throws Exception {

		aosService.saveModel(aosList,acadId);
		return new ResponseResult(true, 200, "保存成功");
	}

}
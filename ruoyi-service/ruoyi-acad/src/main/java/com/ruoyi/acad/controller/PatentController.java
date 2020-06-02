package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Patent;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPatentService;
import com.ruoyi.common.core.domain.RE;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：专利发明信息控制层<br/>
 * CreateTime ：2020年3月31日上午11:13:54<br/>
 * CreateUser：ys<br/>
 */
@RestController
@RequestMapping("/patent")
@Api(tags = "专利和发明管理")
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
	@ApiOperation(value = "新增专利和发明信息")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	public RE saveModel(@Valid @RequestBody@ApiParam(value = "专利和发明列表",required = true) List<Patent> patentList,
						@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.patentService.saveModel(patentList, acadId);
		return new RE().ok("保存成功");
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
	@ApiOperation(value = "修改专利和发明信息")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	public RE updateModel(@Valid @RequestBody@ApiParam(value = "专利和发明列表",required = true) List<Patent> patentList,
						  @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.patentService.updateModel(patentList, acadId);
		return new RE().ok("修改成功");
	}

	/**
	 * Description:获取专利信息
	 * CreateTime:2020年3月31日上午11:15:02
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	@ApiOperation(value = "根据院士编码查询专利和发明信息")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<Patent> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		return this.patentService.getModelById(acadId);
	}
}

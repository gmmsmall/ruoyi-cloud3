package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.Work;
import com.ruoyi.acad.service.IWorkService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/work")
@Api(tags = "院士工作信息表")
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
	@ApiOperation(value = "根据院士id获取对应院士工作信息")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<Work> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		return this.workService.getModelById(acadId);
	}
	
	/**
	 * Description:修改院士工作内容
	 * CreateTime:2020年3月19日上午9:34:50
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	@ApiOperation(value = "修改院士工作信息")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	@OperLog(title = "修改院士工作信息", businessType = BusinessType.UPDATE)
	public RE updateModel(@Valid @RequestBody@ApiParam(value = "院士工作列表",required = true) List<Work> workList,
						  @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.workService.updateModel(workList,acadId);
		return new RE().ok("修改成功");
	}

	/**
	 * Description:保存操作
	 * CreateTime:2020年3月23日上午11:01:14
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	@ApiOperation(value = "新增院士工作信息")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	@OperLog(title = "新增院士工作信息", businessType = BusinessType.INSERT)
	public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士工作列表",required = true) List<Work> workList,
									@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.workService.saveModel(workList,acadId);
		return new RE().ok("保存成功");
	}
}

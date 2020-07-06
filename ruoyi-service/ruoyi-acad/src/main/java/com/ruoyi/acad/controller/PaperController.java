package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Paper;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPaperService;
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
 * Description：创建院士论文控制层<br/>
 * CreateTime ：2020年3月31日上午11:12:13<br/>
 * CreateUser：ys<br/>
 */
@RestController
@RequestMapping("/paper")
@Api(tags = "论文信息管理")
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
	@ApiOperation(value = "新增论文信息")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	@OperLog(title = "新增论文信息", businessType = BusinessType.INSERT)
	public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士论文列表") List<Paper> paperList,
						@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.paperService.saveModel(paperList, acadId);
		return new RE().ok("保存成功");
	}

	/**
	 * Description:修改论文信息
	 * CreateTime:2020年3月31日上午11:08:02
	 * @param paperList
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	@ApiOperation(value = "修改论文信息")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	@OperLog(title = "修改论文信息", businessType = BusinessType.UPDATE)
	public RE updateModel(@Valid @RequestBody@ApiParam(value = "院士论文列表") List<Paper> paperList,
						  @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		this.paperService.updateModel(paperList, acadId);
		return new RE().ok("修改成功");
	}
	
	/**
	 * Description:根据院士ID获取院士论文信息
	 * CreateTime:2020年4月10日下午3:34:38
	 * @param acadId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModelById")
	@ApiOperation(value = "根据院士ID获取院士论文信息")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<Paper> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
		return this.paperService.getModelByAcadId(acadId);
	}
}

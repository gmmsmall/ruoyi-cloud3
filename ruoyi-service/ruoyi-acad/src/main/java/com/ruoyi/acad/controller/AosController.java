package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Aos;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IAosService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "授衔履历管理")
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
	@ApiOperation(value = "根据ID获取对应数据", notes = "院士编码id")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<Aos> getModelById(@RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {
		return this.aosService.getModelById(acadId);
	}
	
	/**
	 * Description：修改授衔履历
	 * CreateTime:2020年3月20日下午1:50:23
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	@ApiOperation(value = "修改授衔履历", notes = "修改履历基本参数")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	@OperLog(title = "修改授衔履历", businessType = BusinessType.UPDATE)
	public RE updateModel(@RequestBody @ApiParam(value = "授衔履历列表") List<Aos> aosList,
						  @ApiParam(value = "院士编码id",required = true) @RequestParam("acadId") Integer acadId) throws Exception {
		this.aosService.updateModel(aosList,acadId);
		return new RE().ok("修改成功");
	}

	/**
	 * Description：新增授衔履历
	 * CreateTime:2020年3月20日下午1:50:23
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveModel")
	@ApiOperation(value = "新增授衔履历", notes = "新增履历基本参数")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	@OperLog(title = "新增授衔履历", businessType = BusinessType.INSERT)
	public RE saveModel(@RequestBody @ApiParam(value = "授衔履历列表") List<Aos> aosList,
									@ApiParam(value = "院士编码id",required = true) @RequestParam("acadId")Integer acadId) throws Exception {

		this.aosService.saveModel(aosList,acadId);
		return new RE().ok("保存成功");
	}

}
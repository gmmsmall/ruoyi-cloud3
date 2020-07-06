package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Email;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IEmailService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：创建邮箱控制层<br/>
 * CreateTime ：2020年3月18日下午1:30:59<br/>
 * CreateUser：ys<br/>	
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/email")
@Api(tags = "邮箱管理")
public class EmailController{

	@Autowired
	private IEmailService emailService;

	/**
	 * Description:查询操作
	 * CreateTime:2020年3月23日上午11:01:14
	 *
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getModel")
	@ApiOperation(value = "根据院士id查询邮箱列表")
	@ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
	public List<Email> getModelById( @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {

		return this.emailService.getModelById(acadId);
	}

	/**
	 * Description:更新操作
	 * CreateTime:2020年3月23日上午11:01:14
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateModel")
	@ApiOperation(value = "更新邮箱列表")
	@ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
	@OperLog(title = "更新邮箱列表", businessType = BusinessType.UPDATE)
	public RE updateModel(@Valid  @RequestBody @ApiParam(value = "邮箱列表") List<Email> emailList,
						  @ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {

		this.emailService.updateModel(emailList,acadId);
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
	@ApiOperation(value = "批量新增邮箱")
	@ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
	@OperLog(title = "批量新增邮箱", businessType = BusinessType.INSERT)
	public RE saveModel(@Valid @RequestBody @ApiParam(value = "邮箱列表") List<Email> emailList,
						@ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {
		this.emailService.saveModel(emailList,acadId);
		return new RE().ok("保存成功");
	}
	
}

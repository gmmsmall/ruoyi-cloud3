package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Email;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(value = "/email", description = "邮箱管理")
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
	@PostMapping("/getModel")
	@ApiOperation(value = "根据院士id查询邮箱列表")
	public List<Email> getModelById(@NotBlank(message = "{required}") Integer acadId) throws Exception {

		List<Email> emailList = emailService.getModelById(acadId);
		return emailList;
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
	public ResponseResult updateModel(@RequestBody List<Email> emailList,@NotBlank Integer acadId) throws Exception {

		emailService.updateModel(emailList,acadId);
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
	@ApiOperation(value = "批量新增邮箱")
	public ResponseResult saveModel(@RequestBody List<Email> emailList,@NotBlank Integer acadId) throws Exception {

		emailService.saveModel(emailList,acadId);
		return new ResponseResult(true, 200, "保存成功");
	}
	
}

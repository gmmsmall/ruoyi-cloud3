package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Education;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IEducationService;
import com.ruoyi.common.core.controller.BaseController;
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
import java.util.Map;

/**
 * Description：教育信息控制层<br/>
 * CreateTime ：2020年3月18日下午2:04:58<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/education")
@Api(tags = "教育信息管理")
public class EducationController extends BaseController {

    @Autowired
    private IEducationService educationService;


    /**
     * Description:根据院士ID获取对应信息
     * CreateTime:2020年3月18日下午2:06:15
     *
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    @ApiOperation(value = "根据院士ID获取对应信息")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Education> getModelById(@RequestParam("acadId") @ApiParam(value = "院士编码id",required = true)Integer acadId) throws Exception {
        return this.educationService.getModelById(acadId);
    }

    /**
     * Description:修改院士教育信息
     * CreateTime:2020年3月19日下午5:32:45
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改院士教育信息")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    @OperLog(title = "修改院士教育信息", businessType = BusinessType.UPDATE)
    public RE updateModel(@Valid @RequestBody @ApiParam(value = "教育信息列表") List<Education> list,
                          @RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {
        this.educationService.updateModel(list,acadId);
        return new RE().ok("修改成功");
    }

    /**
     * Description:保存院士教育信息
     * CreateTime:2020年3月19日下午5:32:45
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    @ApiOperation(value = "新增院士教育信息")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "新增院士教育信息", businessType = BusinessType.INSERT)
    public RE saveModel(@Valid @RequestBody @ApiParam(value = "教育信息列表") List<Education> list,
                        @RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {
        this.educationService.saveModel(list,acadId);
        return new RE().ok("保存成功");
    }
}
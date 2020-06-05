package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Award;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IAwardService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description：院士获奖表控制层<br/>
 * CreateTime ：2020年3月20日下午2:45:15<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/award")
@Api(tags = "荣誉信息管理")
public class AwardController{

    @Autowired
    private IAwardService awardService;

    /**
     * Description:根据ID获取院士获奖信息列表
     * CreateTime:2020年3月20日下午2:49:47
     *
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    @ApiOperation(value = "根据ID获取院士获奖信息列表")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Award> getModelById(@RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {

        return this.awardService.getModelById(acadId);
    }

    /**
     * Description:修改荣誉信息列表
     * CreateTime:2020年3月20日下午3:57:16
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改荣誉信息列表")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    @OperLog(title = "修改荣誉信息列表", businessType = BusinessType.UPDATE)
    public RE updateModel(@RequestBody @ApiParam(value = "荣誉信息列表") List<Award> list,
                          @RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {

        this.awardService.updateModel(list,acadId);
        return new RE().ok("修改成功");
    }

    /**
     * Description:新增荣誉信息列表
     * CreateTime:2020年3月20日下午3:57:16
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    @ApiOperation(value = "新增荣誉信息列表")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "新增荣誉信息列表", businessType = BusinessType.INSERT)
    public RE saveModel(@RequestBody @ApiParam(value = "荣誉信息列表") List<Award> list,
                        @RequestParam("acadId") @ApiParam(value = "院士编码id",required = true) Integer acadId) throws Exception {

        this.awardService.saveModel(list,acadId);
        return new RE().ok("保存成功");
    }
}

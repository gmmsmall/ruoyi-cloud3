package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Nationality;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.INationalityService;
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
@RequestMapping("/nationality")
@Api(tags = "院士国籍管理")
public class NationalityController {


    @Autowired
    private INationalityService nationalityService;

    /**
     * Description:保存国籍信息
     * CreateTime:2020年3月31日上午11:08:02
     * @param nationalityList
     * @return
     * @throws Exception
     */
    @PostMapping("/saveModel")
    @ApiOperation(value = "新增国籍信息")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "新增国籍信息", businessType = BusinessType.INSERT)
    public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士国籍列表",required = true) List<Nationality> nationalityList,
                        @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
        this.nationalityService.saveModel(nationalityList, acadId);
        return new RE().ok("保存成功");
    }

    /**
     * Description:修改国籍信息
     * CreateTime:2020年3月31日上午11:08:02
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改国籍信息")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    @OperLog(title = "修改国籍信息", businessType = BusinessType.UPDATE)
    public RE updateModel(@Valid @RequestBody@ApiParam(value = "院士国籍列表",required = true) List<Nationality> nationalityList,
                          @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
        this.nationalityService.updateModel(nationalityList, acadId);
        return new RE().ok("修改成功");
    }

    /**
     * Description:根据院士ID获取国籍信息
     * CreateTime:2020年4月10日下午3:34:38
     * @param acadId
     * @return
     * @throws Exception
     */
    @GetMapping("/getModelById")
    @ApiOperation(value = "根据院士ID获取国籍信息")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Nationality> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
        return this.nationalityService.getModelById(acadId);
    }
}

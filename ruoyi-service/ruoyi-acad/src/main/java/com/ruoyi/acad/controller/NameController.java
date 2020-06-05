package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Name;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.domain.Sns;
import com.ruoyi.acad.service.INameService;
import com.ruoyi.acad.service.ISnsService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author jxd
 */
@RestController
@RequestMapping("/name")
@Api(tags = "院士姓名管理")
public class NameController {

    @Autowired
    private INameService nameService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getModel")
    @ApiOperation(value = "根据院士id查询")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public Name getModelById(@ApiParam(value = "院士id",required = true)@RequestParam Integer id) throws Exception {
        return this.nameService.getModelById(id);
    }

    /**
     * Description:更新操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改院士姓名")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    @OperLog(title = "修改院士姓名", businessType = BusinessType.UPDATE)
    public RE updateModel(@Valid @RequestBody@ApiParam(value = "院士姓名参数",required = true) Name name,
                          @ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {
        this.nameService.updateModel(name);
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
    @ApiOperation(value = "新增院士姓名")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "新增院士姓名", businessType = BusinessType.INSERT)
    public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士姓名参数",required = true) Name name,
                        @ApiParam(value = "院士编码",required = true)@RequestParam Integer acadId) throws Exception {
        this.nameService.saveModel(name);
        return new RE().ok("保存成功");
    }

}

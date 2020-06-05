package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.Phone;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.service.IPhoneService;
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
@RequestMapping("/phone")
@Api(tags = "院士电话信息表")
public class PhoneController {

    @Autowired
    private IPhoneService phoneService;

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getModel")
    @ApiOperation(value = "根据院士编码查询院士电话信息")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<Phone> getModelById(@ApiParam(value = "院士id",required = true)@RequestParam  Integer id) throws Exception {
        return this.phoneService.getModelById(id);
    }

    /**
     * Description:更新操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改院士电话信息")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    @OperLog(title = "修改院士电话信息", businessType = BusinessType.UPDATE)
    public RE updateModel(@Valid @RequestBody@ApiParam(value = "院士电话列表",required = true) List<Phone> phoneList,
                          @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
        this.phoneService.updateModel(phoneList,acadId);
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
    @ApiOperation(value = "新增院士电话信息")
    @ApiResponses({@ApiResponse(code = 200,message = "保存成功")})
    @OperLog(title = "新增院士电话信息", businessType = BusinessType.INSERT)
    public RE saveModel(@Valid @RequestBody@ApiParam(value = "院士电话列表",required = true) List<Phone> phoneList,
                        @ApiParam(value = "院士id",required = true)@RequestParam Integer acadId) throws Exception {
        this.phoneService.saveModel(phoneList,acadId);
        return new RE().ok("保存成功");
    }

}

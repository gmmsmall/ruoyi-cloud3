package com.ruoyi.acad.controller;

import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.ResponseResult;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.service.IBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Description：创建原始基本信息控制层<br/>
 * CreateTime ：2020年3月11日上午10:09:59<br/>
 * CreateUser：ys<br/>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/baseInfo")
@Api(tags = "院士基础信息管理")
public class BaseInfoController{

    @Autowired
    private IBaseInfoService baseInfoService;

    /**
     * Description:拉黑院士信息
     * CreateTime:2020年3月12日下午1:14:29
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/pullBlack")
    @ApiOperation(value = "院士拉黑", notes = "院士拉黑")
    public ResponseResult PullBlack(@RequestBody BaseInfoForm baseInfoForm) throws Exception {

        baseInfoService.pullBlack(baseInfoForm);
        return new ResponseResult(true, 200, "修改成功");
    }

    /**
     * Description:展厅是否展示
     * CreateTime:2020年3月12日下午1:24:42
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/showBaseInfo")
    @ApiOperation(value = "是否展示", notes = "是否展示")
    public ResponseResult showBaseInfo(@RequestBody BaseInfoForm baseInfoForm) throws Exception {

        baseInfoService.showBaseInfo(baseInfoForm.getAcadId(), baseInfoForm.getIsShow());
        return new ResponseResult(true, 200, "修改成功");
    }

    /**
     * Description:查询操作
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getModel")
    @ApiOperation(value = "查询基本信息", notes = "查询基本信息")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public BaseInfo getModelById(Integer id) throws Exception {
        BaseInfo baseInfo = baseInfoService.getModelById(id);
        return baseInfo;
    }

    /**
     * Description:修改基本信息
     * CreateTime:2020年3月19日上午11:26:01
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateModel")
    @ApiOperation(value = "修改基本信息", notes = "修改基本信息")
    public ResponseResult updateModel(@RequestBody BaseInfo baseInfo) throws Exception {

        baseInfoService.updateBaseInfo(baseInfo);
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
    @ApiOperation(value = "新增基本信息", notes = "新增基本信息")
    public ResponseResult saveModel(@RequestBody BaseInfo baseInfo) throws Exception {

        baseInfoService.saveModel(baseInfo);
        return new ResponseResult(true, 200, "保存成功");
    }

}
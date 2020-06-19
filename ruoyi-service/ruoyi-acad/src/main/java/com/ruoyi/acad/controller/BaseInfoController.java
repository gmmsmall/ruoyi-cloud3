package com.ruoyi.acad.controller;

import com.google.common.base.Joiner;
import com.ruoyi.acad.documnet.ElasticClientAcadRepository;
import com.ruoyi.acad.domain.BaseInfo;
import com.ruoyi.acad.domain.Name;
import com.ruoyi.acad.form.BaseInfoBatch;
import com.ruoyi.acad.form.BaseInfoForm;
import com.ruoyi.acad.service.IBaseInfoService;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.log.annotation.OperLog;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class BaseInfoController {

    @Autowired
    private IBaseInfoService baseInfoService;

    @Autowired
    private ElasticClientAcadRepository elasticClientAcadRepository;

    /**
     * Description:拉黑院士信息
     * CreateTime:2020年3月12日下午1:14:29
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/pullBlack")
    @ApiOperation(value = "院士拉黑", notes = "院士拉黑")
    @ApiResponses({@ApiResponse(code = 200, message = "修改成功")})
    @OperLog(title = "拉黑院士", businessType = BusinessType.BLOCK)
    public RE PullBlack(@RequestBody BaseInfoForm baseInfoForm,
                        @ApiParam(value = "院士编码id", required = true) @RequestParam("acadId") Integer acadId) throws Exception {
        baseInfoService.pullBlack(baseInfoForm);
        return new RE().ok("修改成功");
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
    @ApiResponses({@ApiResponse(code = 200, message = "修改成功")})
    @OperLog(title = "修改院士是否展示", businessType = BusinessType.DISPLAY)
    public RE showBaseInfo(@RequestBody BaseInfoForm baseInfoForm,
                           @ApiParam(value = "院士编码id", required = true) @RequestParam("acadId") Integer acadId) throws Exception {
        baseInfoService.showBaseInfo(baseInfoForm.getAcadId(), baseInfoForm.getIsShow());
        return new RE().ok("修改成功");
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
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功")})
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
    @ApiResponses({@ApiResponse(code = 200, message = "修改成功")})
    public RE updateModel(@RequestBody BaseInfo baseInfo,
                          @ApiParam(value = "院士编码id", required = true) @RequestParam("acadId") Integer acadId) throws Exception {
        baseInfoService.updateBaseInfo(baseInfo,acadId);
        return new RE().ok("修改成功");
    }

    /**
     * Description:删除基本信息
     * CreateTime:2020年6月12日下午15:39:01
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteModel")
    @ApiOperation(value = "删除基本信息", notes = "删除基本信息")
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功")})
    @OperLog(title = "删除院士基本信息", businessType = BusinessType.DELETE)
    public RE deleteModel(@ApiParam(value = "院士编码id", required = true) @RequestParam("acadId") Integer acadId) throws Exception {
        this.baseInfoService.deleteBaseInfo(acadId);
        return new RE().ok("删除成功");
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
    @ApiResponses({@ApiResponse(code = 200, message = "保存成功")})
    public RE saveModel(@RequestBody BaseInfo baseInfo) throws Exception {
        Integer acadId = baseInfoService.saveModel(baseInfo);
        RE re = new RE();
        re.setObject(acadId);
        re.setErrorCode(200);
        re.setStatus(true);
        re.setErrorDesc("保存成功");
        return re;
    }

    /**
     * Description:临时用于删除elasticsearch中某些无用院士信息
     * CreateTime:2020年3月23日上午11:01:14
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteModelOfEs")
    @ApiOperation(value = "删除es中基本信息", notes = "院士编码")
    @ApiResponses({@ApiResponse(code = 200, message = "删除成功")})
    public RE deleteModelOfEs(@RequestParam("acadId") @ApiParam(value = "院士编码id", required = true) String acadId) throws Exception {
        this.elasticClientAcadRepository.deleteById(acadId);
        return new RE().ok("删除成功");
    }

    /**
     * Description:根据院士编码查询院士姓名（中文、英文、原文）
     * CreateTime:2020年6月5日上午09:33:10
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getNameByAcadId")
    @ApiOperation(value = "根据院士编码查询院士姓名", notes = "根据院士编码查询院士姓名")
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功")})
    public RE getNameByAcadId(@ApiParam(value = "院士id", required = true) @RequestParam("acadId") Integer acadId) throws Exception {
        BaseInfo baseInfo = baseInfoService.getModelById(acadId);
        Name name = new Name();
        name.setAcadId(acadId);
        if (baseInfo != null) {
            name.setCnName(baseInfo.getCnName());
            name.setEnName(baseInfo.getEnName());
            name.setRealName(baseInfo.getRealName());
        }
        return RE.ok(name);
    }

    /**
     * Description:根据院士姓名（中文、英文、原文）模糊查询院士编码集合
     * CreateTime:2020年6月5日上午09:36:32
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getAcadListByName")
    @ApiOperation(value = "根据院士姓名模糊查询院士编码集合", notes = "根据院士姓名模糊查询院士编码集合")
    @ApiResponses({@ApiResponse(code = 200, message = "查询成功")})
    public RE getAcadListByName(@ApiParam(value = "院士姓名", required = true) @RequestParam("name") String name) throws Exception {
        List<Integer> acadIs = this.baseInfoService.getAcadListByName(name);
        if (null != acadIs) {
            RE re = RE.ok();
            re.setObject(Joiner.on(",").join(acadIs));
            return re;
        } else {
            return RE.ok();
        }
    }

    /**
     * Description:批量修改院士基本信息
     * CreateTime:2020年6月12日下午15:01:01
     * @return
     * @throws Exception
     */
    @PostMapping("/updateBatchModel")
    @ApiOperation(value = "批量修改院士基本信息", notes = "批量修改院士基本信息")
    @ApiResponses({@ApiResponse(code = 200, message = "修改成功")})
    //@OperLog(title = "批量修改院士基本信息", businessType = BusinessType.UPDATE)
    public RE updateBatchModel(@RequestBody @ApiParam(value = "批量修改院士基本信息") BaseInfoBatch baseInfoBatch) throws Exception {
        this.baseInfoService.updateBatchBaseInfo(baseInfoBatch);
        return new RE().ok("修改成功");
    }

    /**
     * Description:根据院士姓名（中文、英文、原文）模糊查询院士编码集合
     * CreateTime:2020年6月5日上午09:36:32
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/initProfile")
    @ApiOperation(value = "初始化院士简介翻译", notes = "初始化院士简介翻译")
    @ApiResponses({@ApiResponse(code = 200, message = "翻译成功")})
    public RE initProfile(){
        this.baseInfoService.initProfile();
        return new RE().ok("翻译成功");
    }

}
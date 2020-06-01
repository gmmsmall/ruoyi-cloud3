package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ruoyi.common.core.domain.Rest;
import com.ruoyi.common.utils.RestUtil;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.bo.TemplateManagerDeleteBo;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ITemplateManagerService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.vo.TemplateManagerVo;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jxd
 */
@Slf4j
@RestController
@RequestMapping("template")
@Api(tags = "模板管理")
public class TemplateManagerController extends BaseController {

    private String message;

    private boolean flag = true;

    @Autowired
    private ITemplateManagerService templateManagerService;

    /**
     * 获取模板管理列表
      * @param request
     * @param templateManagerBo
     * @return
     * @throws FebsException
     */
    @PostMapping("/list")
    @ApiOperation(value="获取模板管理列表", notes="请求参数：输入模板的基础信息")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功")
    })
    public Rest<IPage<TemplateManager>> templateList(QueryRequest request, @RequestBody TemplateManagerBo templateManagerBo) throws FebsException {
        return RestUtil.success(this.templateManagerService.findTemplate(request,templateManagerBo));
    }

    /**
     * 获取模板管理详情
     * @param id
     * @return
     * @throws FebsException
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value="获取模板管理详情", notes="请求参数：模板id")
    @ApiResponses({@ApiResponse(code = 200,message = "查询详情成功")})
    public Rest<TemplateManagerVo> templateOne(@ApiParam(value = "模板id",required = true)@PathVariable("id") Long id){
        return RestUtil.success(this.templateManagerService.getVoById(id));
    }

    /**
     * 新增一个模板
     * @param templateManagerBo
     * @return
     * @throws FebsException
     */
    @PostMapping("/add")
    @ApiOperation(value="新增一个模板", notes="模板基础数据信息")
    @ApiResponses({@ApiResponse(code = 200,message = "新增成功",response = ResponseResult.class)})
    public Rest addTemplate(@RequestBody TemplateManagerBo templateManagerBo){
        templateManagerBo.setAddperson(FebsUtil.getCurrentUser().getUsername());
        templateManagerBo.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
        templateManagerService.saveT(templateManagerBo);
        return RestUtil.success();
    }

    /**
     * 修改一个模板
     * @param templateManager
     * @return
     * @throws FebsException
     */
    @PostMapping("/edit")
    @ApiOperation(value="修改一个模板", notes="")
    public ResponseResult editTemplate(@RequestBody TemplateManager templateManager) throws FebsException{
        try {
            templateManager.setEdittime(LocalDateTime.now());
            templateManager.setEditperson(FebsUtil.getCurrentUser().getUsername());
            templateManager.setEditpersonid(FebsUtil.getCurrentUser().getUserId());
            templateManager.setDeleteflag("1");//未删除
            //templateManager.setUsenumber(0);//新增时使用次数为0
            templateManagerService.updateTById(templateManager);
            message = "修改成功";
        } catch (Exception e) {
            flag = false;
            message = "修改模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,null);
    }

    /**
     * 删除模板（单删或批量删）
     * @param bo
     * @return
     * @throws FebsException
     */
    @DeleteMapping
    @ApiOperation(value="删除模板（单删或批量删）", notes="请求参数：模板id列表")
    @ApiResponses({@ApiResponse(code = 200,message = "删除成功")})
    public Rest deleteT(@Valid @RequestBody @ApiParam(value = "模板id",required = true) TemplateManagerDeleteBo bo){
        bo.setAddperson(FebsUtil.getCurrentUser().getUsername());
        bo.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
        templateManagerService.deleteTemplates(bo);
        return RestUtil.success();
    }

    /**
     * 导出模板管理列表
     * @param request
     * @param templateManagerBo
     * @param response
     * @throws FebsException
     */
    @PostMapping("/excel")
    @ApiOperation(value="导出模板管理列表", notes="请求参数：各查询条件")
    public void export(QueryRequest request, @RequestBody TemplateManagerBo templateManagerBo, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            List<TemplateManager> list = templateManagerService.findTemplate(request,templateManagerBo).getRecords();
            ExcelKit.$Export(TemplateManager.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出Excel模板管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        //return new ResponseResult(flag,200,message,null);
    }

    /**
     * 获取已发布的邮件模板列表
     * @return
     * @throws FebsException
     */
    @PostMapping("/publisht")
    @ApiOperation(value="获取已发布的邮件模板列表", notes="")
    public ResponseResult publisht() throws FebsException{
        List<TemplateManager> list = new ArrayList<>();
        try{
            LambdaQueryWrapper<TemplateManager> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TemplateManager::getEffective,"1").eq(TemplateManager::getPublish,"1").eq(TemplateManager::getDeleteflag,"1").eq(TemplateManager::getType,"1");
            queryWrapper.orderByDesc(TemplateManager::getEdittime);
            list = templateManagerService.list(queryWrapper);
            message = "获取成功";
        }catch (Exception e){
            flag = false;
            message = "获取已发布的有效模板失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,list);
    }

}

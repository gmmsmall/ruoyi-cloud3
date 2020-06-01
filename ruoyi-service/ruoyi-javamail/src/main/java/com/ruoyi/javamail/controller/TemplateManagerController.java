package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.core.domain.Rest;
import com.ruoyi.common.utils.RestUtil;
import com.ruoyi.javamail.bo.TemplateManagerBo;
import com.ruoyi.javamail.bo.TemplateManagerDeleteBo;
import com.ruoyi.javamail.bo.TemplateManagerEditBo;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.TemplateManager;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ITemplateManagerService;
import com.ruoyi.javamail.util.FebsUtil;
import com.ruoyi.javamail.vo.TemplateManagerPublishVo;
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
    public IPage<TemplateManager> templateList(QueryRequest request, @RequestBody TemplateManagerBo templateManagerBo) throws FebsException {
        return this.templateManagerService.findTemplate(request,templateManagerBo);
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
    public TemplateManagerVo templateOne(@ApiParam(value = "模板id",required = true)@PathVariable("id") Long id){
        return this.templateManagerService.getVoById(id);
    }

    /**
     * 新增一个模板
     * @param templateManagerBo
     * @return
     * @throws FebsException
     */
    @PostMapping("/add")
    @ApiOperation(value="新增一个模板", notes="模板基础数据信息")
    @ApiResponses({@ApiResponse(code = 200,message = "新增成功")})
    public RE addTemplate(@RequestBody TemplateManagerBo templateManagerBo){
        templateManagerBo.setAddperson(FebsUtil.getCurrentUser().getUsername());
        templateManagerBo.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
        this.templateManagerService.saveT(templateManagerBo);
        return new RE().ok();
    }

    /**
     * 修改一个模板
     * @param templateManagerEditBo
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value="修改一个模板", notes="模板基础数据信息")
    @ApiResponses({@ApiResponse(code = 200,message = "修改成功")})
    public RE editTemplate(@RequestBody TemplateManagerEditBo templateManagerEditBo){
        templateManagerEditBo.setEditperson(FebsUtil.getCurrentUser().getUsername());
        templateManagerEditBo.setEditpersonid(FebsUtil.getCurrentUser().getUserId());
        //templateManager.setUsenumber(0);//新增时使用次数为0
        this.templateManagerService.updateTById(templateManagerEditBo);
        return new RE().ok();
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
    public RE deleteT(@Valid @RequestBody @ApiParam(value = "模板id",required = true) TemplateManagerDeleteBo bo){
        bo.setAddperson(FebsUtil.getCurrentUser().getUsername());
        bo.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
        this.templateManagerService.deleteTemplates(bo);
        return new RE().ok();
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
    @ApiResponses({@ApiResponse(code = 200,message = "导出成功")})
    public void export(QueryRequest request, @RequestBody TemplateManagerBo templateManagerBo, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            List<TemplateManager> list = templateManagerService.findTemplate(request,templateManagerBo).getRecords();
            ExcelKit.$Export(TemplateManager.class, response).downXlsx(list, false);
        } catch (Exception e) {
            log.info("导出Excel模板管理失败");
            throw e;
        }
    }

    /**
     * 获取已发布的邮件模板列表
     * @return
     * @throws FebsException
     */
    @GetMapping("/publisht")
    @ApiOperation(value="获取已发布的邮件模板列表", notes="无请求参数")
    @ApiResponses({@ApiResponse(code = 200,message = "查询成功")})
    public List<TemplateManagerPublishVo> publisht(){
        return this.templateManagerService.publisht();
    }

}

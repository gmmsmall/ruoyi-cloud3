package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailItems;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailItemsService;
import com.ruoyi.javamail.util.FebsUtil;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jxd
 */
@RestController
@RequestMapping("mail-items")
@Slf4j
@Api(tags = "邮件发送详情")
public class SendMailItemsController extends BaseController {

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailItemsService sendMailItemsService;

    /**
     * 根据邮件主列表获取邮件发送详情列表
     * @param request
     * @param ids
     * @return
     * @throws FebsException
     */
    @PostMapping("/infoByFid")
    @ApiOperation(value="根据邮件主列表获取邮件发送详情列表", notes="请求参数：邮件id")
    @ApiImplicitParam(paramType="path", name = "id", value = "邮件id", required = true, dataType = "String")
    public ResponseResult infoByFid(QueryRequest request, @RequestBody String ids) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            JSONObject jsonObject = JSONObject.parseObject(ids);
            String id = jsonObject.getString("id");
            if(id != null && !id.equals("")){
                request.setPageNum(1);//默认第一页
                request.setPageSize(10);//默认10条数据
                SendMailItems sendMailItems = new SendMailItems();
                sendMailItems.setFid(Long.parseLong(id));
                sendMailItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
                map.put("list",getDataTable(sendMailItemsService.getSendMailItemsPage(request,sendMailItems)));
                map.put("fid",id);
                message = "获取成功";
            }else{
                flag = false;
                message = "请选择将要获取的数据记录";
            }
        }catch(Exception e){
            flag = false;
            message = "根据邮件主列表获取邮件发送详情列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 获取邮件发送详情列表
     * @param request
     * @param sendMailItems
     * @return
     * @throws FebsException
     */
    @PostMapping("/info")
    @ApiOperation(value="获取邮件发送详情列表", notes="请求参数：邮件详情基础信息")
    public ResponseResult info(QueryRequest request,@RequestBody SendMailItems sendMailItems) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            sendMailItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            map.put("list",getDataTable(sendMailItemsService.getSendMailItemsPage(request,sendMailItems)));
        }catch(Exception e){
            flag = false;
            message = "获取邮件发送详情列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 导出邮件详情列表
     * @param request
     * @param sendMailItems
     * @param response
     * @throws FebsException
     */
    @PostMapping("/export")
    @ApiOperation(value="导出邮件详情列表", notes="请求参数：各查询条件")
    public void export(QueryRequest request, @RequestBody SendMailItems sendMailItems, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            sendMailItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            List<SendMailItems> list = sendMailItemsService.getSendMailItemsPage(request,sendMailItems).getRecords();
            ExcelKit.$Export(SendMailItems.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出Excel邮件详情列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        //return new ResponseResult(flag,200,message,null);
    }

}

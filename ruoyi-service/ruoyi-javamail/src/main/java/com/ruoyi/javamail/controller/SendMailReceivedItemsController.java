package com.ruoyi.javamail.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailReceivedItems;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailReceivedItemsService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gmm
 */
@Slf4j
@RestController
@RequestMapping("mail-received-items")
@Api(tags = "主题邮件接收详情")
public class SendMailReceivedItemsController extends BaseController {

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailReceivedItemsService mailReceivedItemsService;

    /**
     * 获取主题邮件接收子表列表(fid必填)
     * @param request
     * @param sendMailReceivedItems
     * @return
     * @throws FebsException
     */
    @PostMapping("/list")
    @ApiOperation(value="获取主题邮件接收子表列表", notes="请求参数：输入主题邮件详情的基础信息")
    public ResponseResult sendMailReceivedlist(QueryRequest request, @RequestBody SendMailReceivedItems sendMailReceivedItems) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            sendMailReceivedItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            map = getDataTable(mailReceivedItemsService.getReceivedItemsList(request,sendMailReceivedItems));
            message = "获取成功";
        }catch(Exception e){
            flag = false;
            message = "获取主题邮件接收列表子表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 导出主题邮件接收子表列表
     * @param request
     * @param sendMailReceivedItems
     * @param response
     * @throws FebsException
     */
    @PostMapping("/excel")
    @ApiOperation(value="导出主题邮件接收列表", notes="请求参数：各查询条件，fid必填")
    public void export(QueryRequest request, @RequestBody SendMailReceivedItems sendMailReceivedItems, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            sendMailReceivedItems.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            List<SendMailReceivedItems> list = mailReceivedItemsService.getReceivedItemsList(request,sendMailReceivedItems).getRecords();
            ExcelKit.$Export(SendMailReceivedItems.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出主题邮件接收子表列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 获取邮件往来记录
     * @param email
     * @return
     * @throws FebsException
     */
    @PostMapping("/mailexchangelist")
    @ApiOperation(value="获取邮件往来记录", notes="请求参数：邮箱地址")
    @ApiImplicitParam(paramType="path", name = "email", value = "邮箱地址", required = true, dataType = "String")
    public ResponseResult mailexchangelist(@RequestBody String email) throws FebsException {
        List<Map<String,Object>> list = new ArrayList<>();
        try{
            JSONObject jsonObject = JSONObject.parseObject(email);
            String emailstr = jsonObject.getString("email");
            if(emailstr != null && !emailstr.equals("")){
                list = mailReceivedItemsService.getMailExchangebyAcademial(emailstr);
                message = "获取成功";
            }else{
                flag = false;
                message = "请选择将要获取的院士邮箱";
            }
        }catch(Exception e){
            flag = false;
            message = "获取邮件往来记录失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,list);
    }


}

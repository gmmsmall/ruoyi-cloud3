package com.ruoyi.javamail.controller;

import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailReceived;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailReceivedService;
import com.ruoyi.javamail.util.FebsUtil;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
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
 * @author gmm
 */
@Slf4j
@RestController
@RequestMapping("mail-received")
@Api(tags = "主题邮件接收")
public class SendMailReceivedController extends BaseController{

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailReceivedService sendMailReceivedService;

    /**
     * 获取主题邮件接收列表
     * @param request
     * @param sendMailReceived
     * @return
     * @throws FebsException
     */
    @PostMapping("/list")
    @ApiOperation(value="获取主题邮件接收列表", notes="请求参数：输入主题邮件的基础信息")
    public ResponseResult sendMailReceivedlist(QueryRequest request, @RequestBody SendMailReceived sendMailReceived) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            sendMailReceived.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            map = getDataTable(sendMailReceivedService.findSendMailReceived(request,sendMailReceived));
            message = "获取成功";
        }catch(Exception e){
            flag = false;
            message = "获取主题邮件接收列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 导出主题邮件接收列表
     * @param request
     * @param sendMailReceived
     * @param response
     * @throws FebsException
     */
    @PostMapping("/excel")
    @ApiOperation(value="导出主题邮件接收列表", notes="请求参数：各查询条件")
    public void export(QueryRequest request, @RequestBody SendMailReceived sendMailReceived, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            sendMailReceived.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            List<SendMailReceived> list = sendMailReceivedService.findSendMailReceived(request,sendMailReceived).getRecords();
            ExcelKit.$Export(SendMailReceived.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出主题邮件接收列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}

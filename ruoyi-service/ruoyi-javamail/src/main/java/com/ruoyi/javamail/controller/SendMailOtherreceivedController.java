package com.ruoyi.javamail.controller;


import com.ruoyi.javamail.domain.QueryRequest;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailOtherreceived;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailOtherreceivedService;
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
@RequestMapping("mail-otherreceived")
@Api(tags = "其它邮件接收")
public class SendMailOtherreceivedController extends BaseController {

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailOtherreceivedService otherreceivedService;

    /**
     * 获取其它邮件接收列表
     * @param request
     * @param sendMailOtherreceived
     * @return
     * @throws FebsException
     */
    @PostMapping("/list")
    @ApiOperation(value="获取其它邮件接收列表", notes="请求参数：输入其它邮件的基础信息")
    public ResponseResult sendMailReceivedlist(QueryRequest request, @RequestBody SendMailOtherreceived sendMailOtherreceived) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            sendMailOtherreceived.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            map = getDataTable(otherreceivedService.findSendMailOtherReceived(request,sendMailOtherreceived));
            message = "获取成功";
        }catch(Exception e){
            flag = false;
            message = "获取其它邮件接收列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

    /**
     * 导出其它邮件接收列表
     * @param request
     * @param sendMailOtherreceived
     * @param response
     * @throws FebsException
     */
    @PostMapping("/excel")
    @ApiOperation(value="导出其它邮件接收列表", notes="请求参数：各查询条件")
    public void export(QueryRequest request, @RequestBody SendMailOtherreceived sendMailOtherreceived, HttpServletResponse response) throws FebsException {
        try {
            request.setPageNum(1);
            request.setPageSize(Integer.MAX_VALUE);
            sendMailOtherreceived.setAddpersonid(FebsUtil.getCurrentUser().getUserId());
            List<SendMailOtherreceived> list = otherreceivedService.findSendMailOtherReceived(request,sendMailOtherreceived).getRecords();
            ExcelKit.$Export(SendMailOtherreceived.class, response).downXlsx(list, false);
            message = "导出成功";
        } catch (Exception e) {
            flag = false;
            message = "导出其它邮件接收列表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}

package com.ruoyi.javamail.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.javamail.domain.ResponseResult;
import com.ruoyi.javamail.entity.SendMailStmp;
import com.ruoyi.javamail.exception.FebsException;
import com.ruoyi.javamail.service.ISendMailStmpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gmm
 */
@RestController
@RequestMapping("mail-stmp")
@Slf4j
@Api(tags = "Stmp管理")
public class SendMailStmpController extends BaseController {

    private String message;

    private boolean flag = true;

    @Autowired
    private ISendMailStmpService sendMailStmpService;

    /**
     * 获取当天stmp服务器的使用数量
     * @param stmp
     * @return
     * @throws FebsException
     */
    @GetMapping("/getusenumber")
    @ApiOperation(value="获取当天stmp服务器的使用数量", notes="请求参数：stmp名称")
    @ApiImplicitParam(paramType="path", name = "stmp", value = "stmp名称", required = true, dataType = "String")
    public ResponseResult getusenumber(@NotBlank(message = "{required}") String stmp) throws FebsException {
        Map<String,Object> map = new HashMap<>();
        try{
            if(stmp != null && !stmp.equals("")){
                LambdaQueryWrapper<SendMailStmp> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(SendMailStmp::getName, stmp).eq(SendMailStmp::getStmptime, LocalDate.now());
                Map<String,Object> mm = sendMailStmpService.getMap(queryWrapper);
                if(mm != null && !mm.get("usenumber").toString().equals("")){
                    map.put("data",mm.get("usenumber"));
                }else{
                    map.put("data",0);
                }
                message = "获取成功";
            }else{
                flag = false;
                message = "请输入将要查询的stmp名称";
            }
        }catch(Exception e){
            flag = false;
            message = "获取当天stmp服务器的使用数量失败";
            log.error(message, e);
            throw new FebsException(message);
        }
        return new ResponseResult(flag,200,message,map);
    }

}

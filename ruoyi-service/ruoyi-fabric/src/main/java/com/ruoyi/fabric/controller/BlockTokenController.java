package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.fabric.bean.Token;
import com.ruoyi.fabric.service.IBlockOperator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/token")
public class BlockTokenController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;


    @ApiOperation(value = "新增令牌存证", notes = "该接口主要向链上存储令牌信息", httpMethod = "POST" )
    @RequestMapping(value = "/addToken",method = RequestMethod.POST)
    public String add(@RequestBody Token token) {
        System.out.print("开始新增角色信息存证"+token.getName());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(token);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("addToken",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "更新令牌存证", notes = "该接口更新令牌信息", httpMethod = "POST" )
    @RequestMapping(value = "/updateToken",method = RequestMethod.POST)
    public String update(@RequestBody Token token) {
        System.out.print("更新角色信息存证"+token.getName());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(token);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("updateToken",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "遍历令牌信息存证", notes = "该接口主要遍历链上令牌信息", httpMethod = "GET" )
    @RequestMapping(value = "/queryTokens")
    public String query(@RequestBody Map map) {

        String[] args = new String[1];

        String  param= JSON.toJSONString(map);
        args[0] = param;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.query("queryTokens",args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-query-time："+(endtime-starttime)+"毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")){
            result.put("code",1);
        }else{
            JSONObject jsondata =JSONObject.parseObject(data);
            result.put("pageSize",jsondata.get("pageSize"));
            result.put("pageNum",jsondata.get("pageNum"));
            result.put("total",jsondata.get("total"));
            result.put("size",jsondata.get("size"));
            result.put("code",0);
            result.put("tokenList",jsondata.get("tokenList"));
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "删除令牌信息存证", notes = "该接口删除指定id令牌信息", httpMethod = "POST" )
    @RequestMapping(value = "/deleteToken",method = RequestMethod.POST)
    public String delete(@RequestBody Map map) {
        System.out.print("开始新增角色信息存证"+map);
        String[] args = new String[1];
        String  param= JSON.toJSONString(map);
        args[0] = param;
        String data = fabricBlockOperatorService.invoke("deleteToken",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{

            result.put("code",0);

        }
        return JSON.toJSONString(result);
    }

    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

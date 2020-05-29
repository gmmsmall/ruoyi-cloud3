package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.fabric.service.IBlockOperator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/user")
public class BlockUserController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;


    @ApiOperation(value = "用户角色关系存证" , notes = "业务端将用户角色关系上传至区块链，包含新增和更新" , httpMethod = "POST")
    @RequestMapping(value = "/uploadUserRole" , method = RequestMethod.POST)
    public String uploadUserRole(@RequestBody Map map) {
        String[] args = new String[1];

        String param = JSON.toJSONString(map);
        args[0] = param;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.invoke("uploadUserRole" , args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-invoke-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {
            result.put("code" , 0);
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value = "查询用户角色" , notes = "业务端根据用户ID向区块链查询角色列表" , httpMethod = "GET")
    @RequestMapping(value = "/queryUserRole")
    public String queryUserRole(String userId) {
        String[] args = new String[1];
        args[0] = userId;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.query("queryUserRole" , args);
        System.out.println("-----" + args[0]);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-query-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {
            JSONArray jsonArray = JSONArray.parseArray(data);
            result.put("code" , 0);
            result.put("roleList" , jsonArray);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "查询用户令牌信息" , notes = "业务端根据用户ID向区块链查询该用户令牌信息" , httpMethod = "GET")
    @RequestMapping(value = "/queryUserToken")
    public String queryUserToken(String userId) {
        String[] args = new String[1];
        args[0] = userId;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.query("queryUserToken" , args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-query-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {
            JSONArray jsonArray = JSONArray.parseArray(data);
            result.put("code" , 0);
            result.put("tokenList" , jsonArray);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "查询用户科学院信息" , notes = "业务端根据用户ID向区块链查询该用户的科学院权限" , httpMethod = "GET")
    @RequestMapping(value = "/queryUserAos")
    public String queryUserAos(String userId) {
        String[] args = new String[1];
        args[0] = userId;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.query("queryUserAos" , args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-query-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {
            JSONArray jsonArray = JSONArray.parseArray(data);
            result.put("code" , 0);
            result.put("aosList" , jsonArray);
        }
        return JSON.toJSONString(result);

    }
    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

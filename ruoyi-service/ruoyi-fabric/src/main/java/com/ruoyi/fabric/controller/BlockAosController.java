package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.fabric.bean.Aos;
import com.ruoyi.fabric.service.IBlockOperator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/mstaos")
public class BlockAosController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;


    @ApiOperation(value = "新增科学院存证", notes = "该接口主要向链上存储科学院信息", httpMethod = "POST" )
    @RequestMapping(value = "/addAos",method = RequestMethod.POST)
    public String add(@RequestBody Aos aos) {
        System.out.print("开始新增角色信息存证"+aos.getAosCnname());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(aos);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("addAos",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "更新科学院存证", notes = "该接口更新科学院信息", httpMethod = "POST" )
    @RequestMapping(value = "/updateAos",method = RequestMethod.POST)
    public String update(@RequestBody Aos aos) {
        System.out.print("更新科学院信息存证"+aos.getAosCnname());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(aos);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("updateAos",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "遍历科学院信息存证", notes = "该接口主要遍历链上科学院信息", httpMethod = "GET" )
    @RequestMapping(value = "/queryAos")
    public String query() {
        String[] args = new String[1];
        args[0] = "";
        String data = fabricBlockOperatorService.query("queryAos",args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")){
            result.put("code",1);
        }else{
            JSONArray jsonArray = JSONArray.parseArray(data);
            result.put("code",0);
            result.put("aosList",jsonArray);
        }
        return JSON.toJSONString(result);
    }


    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

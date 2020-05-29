package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.fabric.service.IBlockOperator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/acadInfo")
public class BlockAcadController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;


    /*@ApiOperation(value = "新增院士信息存证", notes = "该接口主要向链上存储院士信息摘要merkletree", httpMethod = "POST" )
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public RE add(HttpServletRequest request) {
        System.out.print("开始新增院士信息存证");
        String acadNo= request.getParameter("acadNo").toString();
        String hash= request.getParameter("hash").toString();
        List<List<MerkleNode>> list=new LinkedList();
        List<MerkleNode> leafList=new LinkedList();
        List<String> data = new ArrayList<String>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        System.out.print("院士信息merkle-tree:"+MerkleUtil.getMerkleTree(data));
        String result = fabricAcadService.add(acadNo,MerkleUtil.getMerkleTree(data));
        Map<String, String> map = new HashMap<String, String>();
        //给map中添加元素
        map.put("txid", result);
        return new RE(map);

    }*/

    @ApiOperation(value = "新增院士信息存证" , notes = "该接口主要向链上存储院士信息摘要merkletree" , httpMethod = "POST")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public String add(@RequestBody Map map) {
        String[] args = new String[1];

        String param = JSON.toJSONString(map);
        args[0] = param;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.invoke("save" , args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-invoke-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {

            result.put("code" , 0);
            result.put("txid" , data);
        }
        return JSON.toJSONString(result);

    }

    @ApiOperation(value = "更新院士信息存证" , notes = "该接口主要向链上存储更新的院士信息摘要merkletree" , httpMethod = "POST")
    @RequestMapping(value = "/edit" , method = RequestMethod.POST)
    public String update(@RequestBody Map map) {
        System.out.print("开始更新院士信息存证");
        String[] args = new String[1];

        String param = JSON.toJSONString(map);
        args[0] = param;
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.invoke("edit" , args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-invoke-time：" + (endtime - starttime) + "毫秒");
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code" , 1);
        } else {
            result.put("code" , 0);
            result.put("txid" , data);
        }
        return JSON.toJSONString(result);

    }


    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

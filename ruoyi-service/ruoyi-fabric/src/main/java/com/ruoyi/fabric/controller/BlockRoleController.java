package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.fabric.service.IBlockOperator;
import com.ruoyi.system.domain.RoleForQuery;
import com.ruoyi.system.result.SysRoleResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/role")
public class BlockRoleController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;

    @ApiOperation(value = "获取角色用户Id集合", notes = "根据角色名称模糊查询角色对应的用户集", httpMethod = "GET")
    @RequestMapping(value = "/queryIdsByRoleName")
    public String queryIdsByRoleName(@RequestBody String roleName) {
        String[] args = new String[1];
        args[0] = roleName;
        String data = fabricBlockOperatorService.query("queryIdsByRoleName", args);
        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")) {
            result.put("code", 1);
        } else {
            JSONObject jsondata = JSONObject.parseObject(data);
            result.put("userIds", jsondata.get("userIds"));
            result.put("code", 0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "新增角色信息存证", notes = "该接口主要向链上存储角色信息", httpMethod = "POST")
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public String add(@RequestBody SysRoleResult role) {
        System.out.print("开始新增角色信息存证" + role.getRoleName());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(role);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("addRole", args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")) {
            result.put("code", 1);
        } else {
            result.put("code", 0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "更新角色信息存证", notes = "该接口更新角色信息", httpMethod = "POST")
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public String update(@RequestBody SysRoleResult role) {
        System.out.print("开始更新角色信息存证" + role.getRoleName());
        String[] args = new String[1];
        String fileForwardListJson = JSON.toJSONString(role);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("updateRole", args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")) {
            result.put("code", 1);
        } else {
            result.put("code", 0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "删除角色信息存证", notes = "该接口删除指定id角色信息", httpMethod = "POST")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public String delete(@RequestBody Map map) {
        System.out.print("开始新增角色信息存证" + map);
        String[] args = new String[1];
        String param = JSON.toJSONString(map);
        args[0] = param;
        System.out.println(args[0]);
        String data = fabricBlockOperatorService.invoke("deleteRole", args);

        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")) {
            result.put("code", 1);
            //该角色绑定有用户，返回fail、删除失败
        } else if (data.equals("fail")) {
            result.put("code", 2);
        } else {
            result.put("code", 0);
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value = "遍历角色信息存证", notes = "该接口主要遍历链上角色信息", httpMethod = "GET")
    @RequestMapping(value = "/queryRoles")
    public String query(@RequestBody RoleForQuery roleForQuery) {
        String[] args = new String[1];
        String param = JSON.toJSONString(roleForQuery);
        args[0] = param;
        System.out.println(args[0]);
        Long starttime = System.currentTimeMillis();
        String data = fabricBlockOperatorService.query("queryRoles", args);
        Long endtime = System.currentTimeMillis();
        System.out.println("gateway-query-time：" + (endtime - starttime) + "毫秒");
        System.out.println("xxxxx" + data);
        Map<Object, Object> result = new HashMap<>();
        if (data.equals("error")) {
            result.put("code", 1);
        } else {
            result.put("code", 0);
            JSONObject jsondata = JSONObject.parseObject(data);
            result.put("pageSize", jsondata.get("pageSize"));
            result.put("pageNum", jsondata.get("pageNum"));
            result.put("total", jsondata.get("total"));
            result.put("size", jsondata.get("size"));
            result.put("roleList", jsondata.get("roleList"));
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value = "查询角色的权限", notes = "根据角色ID获取角色的权限", httpMethod = "GET")
    @RequestMapping(value = "/queryRolePerms")
    public String queryRolePerms(String roleId) {
        String[] args = new String[1];
        args[0] = roleId;
        String data = fabricBlockOperatorService.invoke("queryRolePerms", args);
        System.out.println("xxxxx" + data);
        Map<Object, Object> result = new HashMap<>();
        //给map中添加元素
        if (data.equals("error")) {
            result.put("code", 1);
        } else {
            System.out.println("xxxxx" + data);
            JSONObject jsondata = JSONObject.parseObject(data);
            result.put("tokenList", jsondata.get("tokenlist"));
            result.put("aosList", jsondata.get("aoslist"));
            result.put("roleName", jsondata.get("roleName"));
            result.put("code", 0);
            //此部分等待区块链那边传值
            //JSONArray jsonArray = JSONArray.parseArray(data);
            //result.put("code",0);
            //result.put("roleList",jsonArray);
        }
        return JSON.toJSONString(result);
    }
    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

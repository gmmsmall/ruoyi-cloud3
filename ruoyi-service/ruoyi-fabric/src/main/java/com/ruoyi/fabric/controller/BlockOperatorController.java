package com.ruoyi.fabric.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.fabric.service.IBlockOperator;
import com.ruoyi.system.domain.Car;
import com.ruoyi.system.domain.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1")
public class BlockOperatorController {

    @Autowired
    IBlockOperator fabricBlockOperatorService;


    @ApiOperation(value = "数据上链" , notes = "该接口是通过提交交易提案，将汽车数据存储在区块当中，并更新至状态数据库" , httpMethod = "POST")
    @RequestMapping(value = "/insertChainInfo" , method = RequestMethod.POST)
    public ResponseResult insertChainInfo(HttpServletRequest request) {
        String[] args = new String[1];
        Car car = new Car();
        car.setCarNum("CAR15");
        car.setCarName("xwpcar");
        car.setCarBrand("BMW");
        car.setCarColour("red");
        car.setCarOwner("xwp");
        String fileForwardListJson = JSON.toJSONString(car);
        args[0] = fileForwardListJson;
        String data = fabricBlockOperatorService.invoke("createCar" , args);

        return new ResponseResult(data);
    }

    @ApiOperation(value = "获取链上数据" , notes = "该接口主要查询所有汽车数据" , httpMethod = "GET")
    @RequestMapping(value = "/getChainInfo")
    public ResponseResult getChainInfo() {
        String[] args = new String[1];
        String data = fabricBlockOperatorService.query("queryAllCar" , args);
        return new ResponseResult(data);
    }

}

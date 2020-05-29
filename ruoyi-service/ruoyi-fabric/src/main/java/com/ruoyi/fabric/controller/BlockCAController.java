package com.ruoyi.fabric.controller;

import com.ruoyi.fabric.config.NetworkConfig;
import com.ruoyi.fabric.service.IBlockCA;
import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.domain.UserContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1")
public class BlockCAController {

    @Autowired
    IBlockCA fabricCAService;


    @ApiOperation(value = "登记管理员" , notes = "该接口主要是向CA组织登记管理员用户，登记后会CA组织将返回证书等文件" , httpMethod = "GET")
    @RequestMapping(value = "/enrollAdmin")
    public ResponseResult enrollAdmin() {
        System.out.print("进行enrollAdmin");
        String data = fabricCAService.enrollAdmin();
        return new ResponseResult(data);

    }

    @ApiOperation(value = "注册用户" , notes = "该接口主要是向CA组织注册登记用户，注册后该用户能够进行相应组织授权范围内的链上操作" , httpMethod = "GET")
    @RequestMapping(value = "/registerUser")
    public ResponseResult registerUser() throws Exception {
        System.out.print("进行registerUser");
        UserContext userContext = new UserContext();
        userContext.setName("user" + System.currentTimeMillis());
        //userContext.setAffiliation(NetworkConfig.ORG1);
        userContext.setMspId(NetworkConfig.ORG1_MSP);
        String data = fabricCAService.registerUser(userContext);
        return new ResponseResult(data);

    }

    @ApiOperation(value = "重新登记用户证书" , notes = "该接口主要是向CA组织注册登记用户，注册后该用户能够进行相应组织授权范围内的链上操作" , httpMethod = "GET")
    @RequestMapping(value = "/reEnrollUser")
    public ResponseResult reEnrollUser(HttpServletRequest request) throws Exception {
        System.out.print("进行reEnrollUser");
        String username = request.getParameter("username").toString();
        UserContext userContext = new UserContext();
        userContext.setName(username);
        //userContext.setAffiliation(NetworkConfig.ORG1);
        userContext.setMspId(NetworkConfig.ORG1_MSP);
        String data = fabricCAService.reEnroll(userContext);
        return new ResponseResult(data);

    }


    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

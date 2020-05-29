package com.ruoyi.fabric.controller;

import com.ruoyi.fabric.service.IBlockExplorer;
import com.ruoyi.system.domain.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1")
public class BlockExplorerController {

    @Autowired
    IBlockExplorer fabricExplorerService;

    @ApiOperation(value = "hello接口的功能介绍" , notes = "提示接口使用者注意事项" , httpMethod = "GET")
    @RequestMapping(value = "/")
    public ResponseResult hello() {

        String data = "Hello, Mingbyte Blockchain Explorer! ";
        ResponseResult result = new ResponseResult(data);

        return result;
    }

    @ApiOperation(value = "获取链上当前区块高度" , notes = "该接口为区块浏览器提供显示区块高度，即区块的个数" , httpMethod = "GET")
    @RequestMapping(value = "/getBlockHeight")
    public ResponseResult getBlockHeight() {

        long data = fabricExplorerService.getBlockHeight();

        return new ResponseResult(String.valueOf(data));
    }

    @ApiOperation(value = "获取链上当前交易总数量" , notes = "该接口为区块浏览器提供区块交易总数量，即为所有区块内部交易数量的总和" , httpMethod = "GET")
    @RequestMapping(value = "/getTransactionCount")
    public ResponseResult getTransactionCount() {
        long data = fabricExplorerService.getTransactionCount();

        return new ResponseResult(String.valueOf(data));
    }

    @ApiOperation(value = "获取网络基本信息" , notes = "该接口为区块浏览器提供网络基本信息，如通道名称，组织数量，节点数量，通道链码信息" , httpMethod = "GET")
    @RequestMapping(value = "/getBlockBaseInfo")
    public ResponseResult getBlockBaseInfo() throws IOException {
        long data = fabricExplorerService.getBlockBaseInfo();

        return new ResponseResult(String.valueOf(data));
    }

    @ApiOperation(value = "根据区块高度获取区块信息" , notes = "该接口为区块浏览器提供根据区块高度获取区块信息" , httpMethod = "POST")
    @RequestMapping(value = "/getBlockInfoByheight" , method = RequestMethod.POST)
    public ResponseResult getBlockInfoByheight(HttpServletRequest request) {
        long height = Long.parseLong(request.getParameter("height").toString());

        System.out.println("进入getBlockInfoByheight" + height);
        String data = fabricExplorerService.getBlockInfoByheight(height);

        return new ResponseResult(String.valueOf(data));
    }

    // TODO: 1. 调用service层，提供其他的接口。
    // TODO: 2. 完成controller层单元测试，参考 BlockExplorerControllerTest。
}

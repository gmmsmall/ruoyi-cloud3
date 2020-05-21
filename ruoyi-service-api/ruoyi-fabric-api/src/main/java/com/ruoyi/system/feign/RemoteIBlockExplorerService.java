package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.factory.RemoteIBlockExplorerFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockExplorerFallbackFactory.class)
public interface RemoteIBlockExplorerService {

    // hello接口的功能介绍
    @GetMapping("/api/v1")
    ResponseResult hello();

    // 获取链上当前区块高度
    @GetMapping("/api/v1/getBlockHeight")
    ResponseResult getBlockHeight();

    // 获取链上当前交易总数量
    @GetMapping("/api/v1/getTransactionCount")
    ResponseResult getTransactionCount();

    // 获取网络基本信息
    @GetMapping("/api/v1/getBlockBaseInfo")
    ResponseResult getBlockBaseInfo();

    // 根据区块高度获取区块信息
    @PostMapping("/api/v1/getBlockInfoByheight")
    ResponseResult getBlockInfoByheight(@RequestParam("request") HttpServletRequest request);
}

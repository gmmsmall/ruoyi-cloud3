package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.factory.RemoteIBlockOperatorFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockOperatorFallbackFactory.class)
public interface RemoteIBlockOperatorService {

    // 数据上链
    @PostMapping("/api/v1/insertChainInfo")
    ResponseResult insertChainInfo();

    // 获取链上数据
    @PostMapping("/api/v1/getChainInfo")
    ResponseResult getChainInfo();
}

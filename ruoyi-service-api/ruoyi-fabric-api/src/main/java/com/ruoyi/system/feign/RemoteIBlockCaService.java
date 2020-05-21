package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.factory.RemoteIBlockCAFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockCAFallbackFactory.class)
public interface RemoteIBlockCaService {

    // 登记管理员
    @GetMapping("/api/v1/enrollAdmin")
    ResponseResult enrollAdmin();

    // 注册用户
    @GetMapping("/api/v1/registerUser")
    ResponseResult registerUser();

    // 重新登记用户证书
    @GetMapping("/api/v1/reEnrollUser")
    ResponseResult reEnrollUser(@RequestParam("request") HttpServletRequest request);
}

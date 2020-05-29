package com.ruoyi.system.feign;


import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.Token;
import com.ruoyi.system.domain.TokenForQuery;
import com.ruoyi.system.feign.factory.RemoteIBlockTokenFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockTokenFallbackFactory.class)
public interface RemoteIBlockTokenService {

    // 新增令牌存证
    @PostMapping("/token/addToken")
    String addToken(@RequestBody Token token);

    // 更新令牌存证
    @PostMapping("/token/updateToken")
    String updateToken(@RequestBody Token token);

    // 遍历令牌信息存证
    @GetMapping("/token/queryTokens")
    String queryTokens(@RequestBody TokenForQuery tokenForQuery);

    // 删除令牌信息存证
    @GetMapping("/token/deleteToken")
    String deleteToken(@RequestBody Map map);
}

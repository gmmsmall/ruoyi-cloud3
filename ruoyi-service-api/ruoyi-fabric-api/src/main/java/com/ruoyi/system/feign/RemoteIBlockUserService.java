package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.feign.factory.RemoteIBlockUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockUserFallbackFactory.class)
public interface RemoteIBlockUserService {

    // 用户角色关系存证
    @PostMapping("/user/uploadUserRole")
    String uploadUserRole(@RequestBody Map map);

    // 查询用户角色
    @GetMapping("/user/queryUserRole")
    String queryUserRole(@RequestParam("userId") String userId);

    // 查询用户令牌信息
    @GetMapping("/user/queryUserToken")
    String queryUserToken(@RequestParam("userId") String userId);

    // 查询用户科学院信息
    @GetMapping("/user/queryUserAos")
    String queryUserAos(@RequestParam("userId") String userId);
}

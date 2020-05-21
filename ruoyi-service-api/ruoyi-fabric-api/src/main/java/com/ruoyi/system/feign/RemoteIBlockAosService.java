package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.feign.factory.RemoteIBlockAcadFallbackFactory;
import com.ruoyi.system.feign.factory.RemoteIBlockAosFallbackFactory;
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
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockAosFallbackFactory.class)
public interface RemoteIBlockAosService {

    // 新增科学院存证
    @PostMapping("/mstaos/addAos")
    String addAos(@RequestBody Aos aos);

    // 更新科学院存证
    @PostMapping("/mstaos/updateAos")
    String updateAos(@RequestBody Aos aos);

    // 遍历科学院信息存证
    @GetMapping("/mstaos/queryAos")
    String queryAos();
}

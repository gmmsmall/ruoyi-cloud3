package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.feign.factory.RemoteIBlockAcadFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockAcadFallbackFactory.class)
public interface RemoteIBlockAcadService {

    // 院士信息链上存证
    @PostMapping("/acadInfo/save")
    String saveAcad(@RequestBody Map map);

    // 院士信息链上更新
    @PostMapping("/acadInfo/edit")
    String updateAcad(@RequestBody Map map);

}

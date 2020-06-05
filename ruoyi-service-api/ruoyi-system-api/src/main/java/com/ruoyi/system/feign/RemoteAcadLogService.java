package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.feign.factory.RemoteAcadLogFallbackFactory;
import com.ruoyi.system.feign.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 日志Feign服务层
 * 
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.MB_SYSTEM_SERVICE, fallbackFactory = RemoteAcadLogFallbackFactory.class)
public interface RemoteAcadLogService
{
    @PostMapping("acadLog/save")
    public void insertOperlog(@RequestBody AcadOperLog operLog);
}

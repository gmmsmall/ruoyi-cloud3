package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.feign.RemoteAcadLogService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteAcadLogFallbackFactory implements FallbackFactory<RemoteAcadLogService> {
    @Override
    public RemoteAcadLogService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteAcadLogService() {
            @Override
            public void insertOperlog(AcadOperLog operLog) {

            }
        };
    }
}

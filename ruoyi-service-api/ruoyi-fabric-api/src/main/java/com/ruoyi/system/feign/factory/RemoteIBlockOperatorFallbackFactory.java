package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.RemoteIBlockOperatorService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteIBlockOperatorFallbackFactory implements FallbackFactory<RemoteIBlockOperatorService> {

    @Override
    public RemoteIBlockOperatorService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockOperatorService() {

            @Override
            public ResponseResult insertChainInfo() {
                return null;
            }

            @Override
            public ResponseResult getChainInfo() {
                return null;
            }
        };
    }
}

package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.feign.RemoteIBlockAosService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteIBlockAosFallbackFactory implements FallbackFactory<RemoteIBlockAosService> {

    @Override
    public RemoteIBlockAosService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockAosService() {
            @Override
            public String addAos(Aos aos) {
                return null;
            }

            @Override
            public String updateAos(Aos aos) {
                return null;
            }

            @Override
            public String queryAos(String aosContinent) {
                return null;
            }
        };
    }
}

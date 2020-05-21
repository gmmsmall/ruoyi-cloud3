package com.ruoyi.system.feign.factory;

import com.ruoyi.system.feign.RemoteIBlockAcadService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RemoteIBlockAcadFallbackFactory implements FallbackFactory<RemoteIBlockAcadService> {/* (non-Javadoc)
 * @see feign.hystrix.FallbackFactory#create(java.lang.Throwable)
 */

    @Override
    public RemoteIBlockAcadService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockAcadService() {

            @Override
            public String saveAcad(Map map) {
                return null;
            }

            @Override
            public String updateAcad(Map map) {
                return null;
            }
        };
    }
}

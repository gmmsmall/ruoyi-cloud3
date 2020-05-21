package com.ruoyi.system.feign.factory;

import com.ruoyi.system.feign.RemoteIBlockUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RemoteIBlockUserFallbackFactory implements FallbackFactory<RemoteIBlockUserService> {/* (non-Javadoc)
 * @see feign.hystrix.FallbackFactory#create(java.lang.Throwable)
 */

    @Override
    public RemoteIBlockUserService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockUserService() {

            @Override
            public String uploadUserRole(Map map) {
                return null;
            }

            @Override
            public String queryUserRole(String userId) {
                return null;
            }

            @Override
            public String queryUserToken(String userId) {
                return null;
            }

            @Override
            public String queryUserAos(String userId) {
                return null;
            }
        };
    }
}

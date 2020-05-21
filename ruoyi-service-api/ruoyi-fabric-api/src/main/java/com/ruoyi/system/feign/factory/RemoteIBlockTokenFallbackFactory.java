package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.Token;
import com.ruoyi.system.feign.RemoteIBlockTokenService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RemoteIBlockTokenFallbackFactory implements FallbackFactory<RemoteIBlockTokenService> {

    @Override
    public RemoteIBlockTokenService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockTokenService(){

            @Override
            public String addToken(Token token) {
                return null;
            }

            @Override
            public String updateToken(Token token) {
                return null;
            }

            @Override
            public String queryTokens(Map map) {
                return null;
            }

            @Override
            public String deleteToken(Map map) {
                return null;
            }
        };
    }
}

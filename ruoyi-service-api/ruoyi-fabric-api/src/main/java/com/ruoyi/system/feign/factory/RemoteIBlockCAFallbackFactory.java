package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.RemoteIBlockCaService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RemoteIBlockCAFallbackFactory implements FallbackFactory<RemoteIBlockCaService> {

    @Override
    public RemoteIBlockCaService create(Throwable throwable) {
        log.error(throwable.getMessage());

        return new RemoteIBlockCaService() {
            @Override
            public ResponseResult enrollAdmin() {
                return null;
            }

            @Override
            public ResponseResult registerUser() {
                return null;
            }

            @Override
            public ResponseResult reEnrollUser(HttpServletRequest request) {
                return null;
            }
        };
    }
}

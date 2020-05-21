package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.ResponseResult;
import com.ruoyi.system.feign.RemoteIBlockExplorerService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RemoteIBlockExplorerFallbackFactory implements FallbackFactory<RemoteIBlockExplorerService> {

    @Override
    public RemoteIBlockExplorerService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockExplorerService() {

            @Override
            public ResponseResult hello() {
                return null;
            }

            @Override
            public ResponseResult getBlockHeight() {
                return null;
            }

            @Override
            public ResponseResult getTransactionCount() {
                return null;
            }

            @Override
            public ResponseResult getBlockBaseInfo() {
                return null;
            }

            @Override
            public ResponseResult getBlockInfoByheight(HttpServletRequest request) {
                return null;
            }
        };
    }
}

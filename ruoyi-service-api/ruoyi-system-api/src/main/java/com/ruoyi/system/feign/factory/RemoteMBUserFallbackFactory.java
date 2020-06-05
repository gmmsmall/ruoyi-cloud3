package com.ruoyi.system.feign.factory;

import com.ruoyi.common.core.domain.RE;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.RemoteMBUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class RemoteMBUserFallbackFactory implements FallbackFactory<RemoteMBUserService> {

    @Override
    public RemoteMBUserService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteMBUserService() {
            @Override
            public RE selectSysUserByUsername(String username) {
                return null;
            }

            @Override
            public RE getAosPerms(String token) {
                return null;
            }

        };
    }
}

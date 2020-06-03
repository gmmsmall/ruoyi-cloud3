package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.RemoteMBUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteMBUserFallbackFactory implements FallbackFactory<RemoteMBUserService> {

    @Override
    public RemoteMBUserService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteMBUserService() {
            @Override
            public SysUser selectSysUserByUsername(String username) {
                return null;
            }

            @Override
            public SysUser getUser() {
                return null;
            }

            @Override
            public List<Aos> updateUserLoginRecord(String token) {
                return null;
            }

            @Override
            public SysUser selectSysUserByUserId(long userId) {
                SysUser user = new SysUser();
                user.setUserId(0l);
                user.setLoginName("no user");
                return user;
            }
        };
    }
}

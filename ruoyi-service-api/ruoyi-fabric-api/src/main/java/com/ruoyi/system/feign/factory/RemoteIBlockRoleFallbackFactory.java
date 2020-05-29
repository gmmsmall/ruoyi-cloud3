package com.ruoyi.system.feign.factory;

import com.ruoyi.system.domain.RoleForQuery;
import com.ruoyi.system.feign.RemoteIBlockRoleService;
import com.ruoyi.system.result.SysRoleResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RemoteIBlockRoleFallbackFactory implements FallbackFactory<RemoteIBlockRoleService> {

    @Override
    public RemoteIBlockRoleService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteIBlockRoleService() {

            @Override
            public String addRole(SysRoleResult role) {
                return null;
            }

            @Override
            public String updateRole(SysRoleResult role) {
                return null;
            }

            @Override
            public String deleteRole(Map map) {
                return null;
            }

            @Override
            public String queryRoles(RoleForQuery roleForQuery) {
                return null;
            }

            @Override
            public String queryRolePerms(String roleId) {
                return null;
            }
        };
    }
}

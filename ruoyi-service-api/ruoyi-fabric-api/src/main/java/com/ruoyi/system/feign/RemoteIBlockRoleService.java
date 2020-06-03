package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.system.domain.RoleForQuery;
import com.ruoyi.system.feign.factory.RemoteIBlockRoleFallbackFactory;
import com.ruoyi.system.result.SysRoleResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.ACAD_FABRIC_SERVICE, fallbackFactory = RemoteIBlockRoleFallbackFactory.class)
public interface RemoteIBlockRoleService {

    // 新增角色信息存证
    @PostMapping("/role/addRole")
    String addRole(@RequestBody SysRoleResult role);

    // 更新角色信息存证
    @PostMapping("/role/updateRole")
    String updateRole(@RequestBody SysRoleResult role);

    // 删除角色信息存证
    @PostMapping("/role/deleteRole")
    String deleteRole(@RequestBody Map map);

    // 遍历角色信息存证
    @GetMapping("/role/queryRoles")
    String queryRoles(@RequestBody RoleForQuery roleForQuery);

    // 查询角色的权限
    @GetMapping("/role/queryRolePerms")
    String queryRolePerms(@RequestParam("roleId") String roleId);

    // 根据角色名称模糊查询角色对应的用户集
    @GetMapping("/role/queryIdsByRoleName")
    String queryIdsByRoleName(@RequestParam("roleName") String roleName);
}

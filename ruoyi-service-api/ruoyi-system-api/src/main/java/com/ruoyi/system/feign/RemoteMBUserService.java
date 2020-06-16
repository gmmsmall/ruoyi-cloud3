package com.ruoyi.system.feign;

import com.ruoyi.common.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.factory.RemoteMBUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户 Feign服务层
 *
 * @author jxd
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.MB_SYSTEM_SERVICE, fallbackFactory = RemoteMBUserFallbackFactory.class)
public interface RemoteMBUserService {

    @GetMapping("user/find/{username}")
    public RE selectSysUserByUsername(@PathVariable("username") String username);

    /**
     * @Author jxd
     * @Description 根据token获取当前用户的科学院信息权限
     * @Date 10:40 2020/6/5
     * @Param [token]
     * @return java.util.List<com.ruoyi.system.domain.Aos>
     **/
    @GetMapping("user/getAosPerms")
    public RE getAosPerms(@RequestParam("token") String token);

    /**
     * @Author jxd
     * @Description 获取当前用户的菜单权限
     * @Date 10:40 2020/6/5
     * @Param [token]
     * @return java.util.List<com.ruoyi.system.domain.Aos>
     **/
    @GetMapping("user/getTokenPerms")
    public RE getTokenPerms(@RequestParam("userId")Long userId);
}

package com.ruoyi.common.auth.aspect;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.auth.annotation.HasPermissions;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.exception.ForbiddenException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.system.feign.RemoteMBUserService;
import com.ruoyi.system.feign.RemoteMenuService;
import com.ruoyi.system.result.PermsResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class PreAuthorizeAspect {
    @Autowired
    private RemoteMenuService sysMenuClient;
    @Autowired
    private RemoteMBUserService remoteMBUserService;

    @Around("@annotation(com.ruoyi.common.auth.annotation.HasPermissions)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        HasPermissions annotation = method.getAnnotation(HasPermissions.class);
        if (annotation == null) {
            return point.proceed();
        }
        String authority = new StringBuilder(annotation.value()).toString();
        if (has(authority)) {
            return point.proceed();
        } else {
            throw new ForbiddenException();
        }
    }

    private boolean has(String authority) {
        // 用超管帐号方便测试，拥有所有权限
        HttpServletRequest request = ServletUtils.getRequest();
        String tmpUserKey = request.getHeader(Constants.CURRENT_ID);
        if (Optional.ofNullable(tmpUserKey).isPresent()) {
            Long userId = Long.valueOf(tmpUserKey);
            log.debug("userid:{}", userId);
//            if (userId == 1L)
//            {
//                return true;
//            }
            // 查询用户信息
            RE re = remoteMBUserService.getTokenPerms(userId);
            List<PermsResult> permsResults = null;
            if (null != re && null != re.getObject()) {
                permsResults = JSON.parseArray(JSON.toJSONString(re.getObject()), PermsResult.class);
            }
            if (permsResults != null) {
                for (PermsResult p : permsResults) {
                    for (String s : p.getPerm()) {
                        if (s.equals(authority)) {
                            return true;
                        }
                    }
                }
                if (permsResults.contains(authority)) {
                    return true;
                } else {
                    return false;
                }
            }
//            return sysMenuClient.selectPermsByUserId(userId).stream().anyMatch(authority::equals);
        }
        return false;
    }
}
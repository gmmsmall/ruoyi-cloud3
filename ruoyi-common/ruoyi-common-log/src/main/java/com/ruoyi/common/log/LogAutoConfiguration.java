package com.ruoyi.common.log;

import com.ruoyi.common.log.aspect.OperLogAspect;
import com.ruoyi.common.log.listen.LogListener;
import com.ruoyi.system.feign.RemoteAcadLogService;
import com.ruoyi.system.feign.RemoteLogService;
import com.ruoyi.system.feign.RemoteUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {
    private final RemoteLogService logService;

    private final RemoteAcadLogService acadLogService;

    @Bean
    public LogListener sysOperLogListener() {
        return new LogListener(logService, acadLogService);
    }

    @Bean
    public OperLogAspect operLogAspect() {
        return new OperLogAspect();
    }
}
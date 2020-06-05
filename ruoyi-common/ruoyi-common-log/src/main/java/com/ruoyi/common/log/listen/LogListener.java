package com.ruoyi.common.log.listen;

import com.ruoyi.common.log.event.AcadOperLogEvent;
import com.ruoyi.common.log.event.SysLogininforEvent;
import com.ruoyi.common.log.event.SysOperLogEvent;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.feign.RemoteAcadLogService;
import com.ruoyi.system.feign.RemoteLogService;
import com.ruoyi.system.feign.RemoteUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class LogListener {
    private final RemoteLogService remoteLogService;

    private final RemoteAcadLogService remoteAcadLogService;

    @Async
    @Order
    @EventListener(SysOperLogEvent.class)
    public void listenOperLog(SysOperLogEvent event) {
        SysOperLog sysOperLog = (SysOperLog) event.getSource();
        remoteLogService.insertOperlog(sysOperLog);
        log.info("远程操作日志记录成功：{}", sysOperLog);
    }

    @Async
    @Order
    @EventListener(SysLogininforEvent.class)
    public void listenLoginifor(SysLogininforEvent event) {
        SysLogininfor sysLogininfor = (SysLogininfor) event.getSource();
        remoteLogService.insertLoginlog(sysLogininfor);
        log.info("远程访问日志记录成功：{}", sysLogininfor);
    }

    @Async
    @Order
    @EventListener(AcadOperLogEvent.class)
    public void listenAcadOperLog(AcadOperLogEvent event) {
        AcadOperLog acadOperLog = (AcadOperLog) event.getSource();
        remoteAcadLogService.insertOperlog(acadOperLog);
        log.info("远程操作日志记录成功：{}", acadOperLog);
    }
}
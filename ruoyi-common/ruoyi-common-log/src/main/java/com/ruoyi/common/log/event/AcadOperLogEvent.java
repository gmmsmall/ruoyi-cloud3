package com.ruoyi.common.log.event;

import com.ruoyi.system.domain.AcadOperLog;
import com.ruoyi.system.domain.SysOperLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 */
public class AcadOperLogEvent extends ApplicationEvent
{
    //
    private static final long serialVersionUID = 8905017895058642111L;

    public AcadOperLogEvent(AcadOperLog source)
    {
        super(source);
    }
}
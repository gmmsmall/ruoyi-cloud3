package com.ruoyi.common.exception;

import com.ruoyi.common.core.domain.RE;
import com.ruoyi.common.core.domain.Rest;
import com.ruoyi.common.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author zmr
 * @author lucas
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public Rest handleException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException异常");
        return RestUtil.error(500,"不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Rest notFount(RuntimeException e) {
        log.error("RuntimeException异常");
        return RestUtil.error(500,"运行时异常:" + e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RuoyiException.class)
    public Rest handleWindException(RuoyiException e) {
        return RestUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Rest handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return RestUtil.error(500,"数据库中已存在该记录");
    }

    @ExceptionHandler(Exception.class)
    public Rest handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);
        return RestUtil.error(500, "参数或服务器异常，请联系管理员");
    }

    /**
     * 捕获并处理未授权异常
     *
     * @param e 授权异常
     * @return 统一封装的结果类, 含有代码code和提示信息msg
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Rest handle401(UnauthorizedException e) {
        return RestUtil.error(401, e.getMessage());
    }

    // 验证码错误
    @ExceptionHandler(ValidateCodeException.class)
    public Rest handleCaptcha(ValidateCodeException e) {
        return RestUtil.error(500,e.getMessage());
    }
}
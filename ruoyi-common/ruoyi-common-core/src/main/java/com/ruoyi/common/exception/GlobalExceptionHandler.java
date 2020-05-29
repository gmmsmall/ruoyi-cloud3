package com.ruoyi.common.exception;

import com.ruoyi.common.core.domain.RE;
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
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public RE handleException(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return RE.error(500, "不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public RE notFount(RuntimeException e) {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        logger.error("运行时异常:", e);
        return RE.error(500, "运行时异常:" + e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RuoyiException.class)
    public RE handleWindException(RuoyiException e) {
        return RE.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public RE handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return RE.error("数据库中已存在该记录");
    }

    @ExceptionHandler(Exception.class)
    public RE handleException(Exception e) throws Exception {
        logger.error(e.getMessage(), e);
        return RE.error(500, "服务器错误，请联系管理员");
    }

    /**
     * 捕获并处理未授权异常
     *
     * @param e 授权异常
     * @return 统一封装的结果类, 含有代码code和提示信息msg
     */
    @ExceptionHandler(UnauthorizedException.class)
    public RE handle401(UnauthorizedException e) {
        return RE.error(401, e.getMessage());
    }

    // 验证码错误
    @ExceptionHandler(ValidateCodeException.class)
    public RE handleCaptcha(ValidateCodeException e) {
        return RE.error(e.getMessage());
    }
}
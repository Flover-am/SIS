package com.seciii.prism063.common.handler;

import cn.dev33.satoken.exception.SaTokenException;
import com.seciii.prism063.common.Result;
import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /* ===============未知异常================ */
    /**
     * 处理不可控异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(ErrorType.SERVER_ERROR.getCode(), e.getMessage());
    }

    /* ===============业务异常================ */
    /**
     * 处理用户异常
     *
     * @param e 业务异常
     * @return 响应结果
     */
    @ExceptionHandler(UserException.class)
    public Result<Void> handleUserException(UserException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(e.getErrorType().getCode(), e.getMessage());
    }

    /**
     * 处理satoken鉴权异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(SaTokenException.class)
    public Result<Void> handleUSaTokenException(SaTokenException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMessage());
    }
}

package com.seciii.prism030.common.handler;

import cn.dev33.satoken.exception.SaTokenException;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.common.exception.GraphException;
import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.UserException;
import com.seciii.prism030.common.exception.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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
    public Result<Void> handleSaTokenException(SaTokenException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(ErrorType.UNAUTHORIZED.getCode(), e.getMessage());
    }

    /**
     * 处理新闻异常
     * @param e 新闻异常
     * @return 响应结果
     */
    @ExceptionHandler(NewsException.class)
    public Result<Void> handleNewsException(NewsException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(e.getErrorType().getCode(), e.getMessage());
    }

    /**
     * 处理知识图谱异常
     * @param e 新闻异常
     * @return 响应结果
     */
    @ExceptionHandler(GraphException.class)
    public Result<Void> handleGraphException(GraphException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(e.getErrorType().getCode(), e.getMessage());
    }


    /**
     * 处理数据格式异常
     *
     * @param e 数据格式异常
     * @return 响应结果
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        if (e.hasErrors()) {
            FieldError fieldError = e.getFieldError();
            if (fieldError != null) {
                String message = fieldError.getDefaultMessage();
                return Result.error(ErrorType.ILLEGAL_ARGUMENTS.getCode(), message);
            }
        }
        return Result.error(ErrorType.UNKNOWN_ERROR.getCode(), ErrorType.UNKNOWN_ERROR.getMessage());
    }

    /* ===============未知异常================ */
    /**
     * 处理不可控异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(ErrorType.SERVER_ERROR.getCode(), "未知错误");
    }
}

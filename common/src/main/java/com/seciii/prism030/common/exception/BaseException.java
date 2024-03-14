package com.seciii.prism030.common.exception;

import com.seciii.prism030.common.exception.error.ErrorType;
import lombok.Getter;

/**
 * 基础异常类
 *
 * @author xueruichen
 * @date 2024.03.05
 */
@Getter
public abstract class BaseException extends RuntimeException{
    /**
     * 状态码
     */
    private final int code;
    /**
     * 异常信息
     */
    private final String message;
    /**
     * http状态码
     */
    private final int httpStatus;
    /**
     * 错误类型
     */
    private final ErrorType errorType;

     BaseException(ErrorType type) {
        this.errorType = type;
        this.code = type.getCode();
        this.message = type.getMessage();
        this.httpStatus = type.getHttpStatus();
    }

    BaseException(ErrorType type, String message) {
        this.errorType = type;
        this.code = type.getCode();
        this.message = message;
        this.httpStatus = type.getHttpStatus();
    }
}

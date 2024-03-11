package com.seciii.prism063.common.exception;

import com.seciii.prism063.common.enums.ErrorType;
import lombok.Getter;

@Getter
public class NewsException extends RuntimeException {
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
    private final int httpCode;
    /**
     * 错误类型
     */
    private final ErrorType errorType;

    public NewsException(ErrorType type) {
        this.errorType = type;
        this.code = type.getCode();
        this.message = type.getMessage();
        this.httpCode = type.getHttpCode();
    }
    public NewsException(ErrorType type, String message) {
        this.errorType = type;
        this.code = type.getCode();
        this.message = message;
        this.httpCode = type.getHttpCode();
    }
}

package com.seciii.prism030.common.exception;

import com.seciii.prism030.common.exception.error.ErrorType;

/**
 * 用户业务异常类
 *
 * @author xueruichen
 * @date 2024.04.10
 */
public class UserException extends BaseException {
    public UserException(ErrorType type) {
        super(type);
    }

    public UserException(ErrorType type, String message) {
        super(type, message);
    }
}

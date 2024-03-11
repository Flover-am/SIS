package com.seciii.prism063.common.exception;

import com.seciii.prism063.common.exception.error.ErrorType;

public class UserException extends BaseException {
    public UserException(ErrorType type) {
        super(type);
    }

    public UserException(ErrorType type, String message) {
        super(type, message);
    }
}

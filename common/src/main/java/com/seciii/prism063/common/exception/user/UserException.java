package com.seciii.prism063.common.exception.user;

import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.base.BaseException;

public class UserException extends BaseException {
    public UserException(ErrorType type) {
        super(type);
    }

    public UserException(ErrorType type, String message) {
        super(type, message);
    }
}

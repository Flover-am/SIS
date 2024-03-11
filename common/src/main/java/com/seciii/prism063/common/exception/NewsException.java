package com.seciii.prism063.common.exception;

import com.seciii.prism063.common.exception.error.ErrorType;
import lombok.Getter;

@Getter
public class NewsException extends BaseException {
    public NewsException(ErrorType type) {
        super(type);
    }

    public NewsException(ErrorType type, String message) {
        super(type, message);
    }
}

package com.seciii.prism030.common.exception;

import com.seciii.prism030.common.exception.error.ErrorType;
import lombok.Getter;

/**
 * 新闻业务异常类
 *
 * @author xueruichen
 * @date 2024.04.10
 */
@Getter
public class NewsException extends BaseException {
    public NewsException(ErrorType type) {
        super(type);
    }

    public NewsException(ErrorType type, String message) {
        super(type, message);
    }
}

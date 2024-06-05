package com.seciii.prism030.common.exception;

import com.seciii.prism030.common.exception.error.ErrorType;

/**
 * 大模型模块异常
 *
 * @author xueruichen
 * @date 2024.05.28
 */
public class LLMException extends BaseException{
    public LLMException(ErrorType type) {
        super(type);
    }

    public LLMException(ErrorType type, String message) {
        super(type, message);
    }
}

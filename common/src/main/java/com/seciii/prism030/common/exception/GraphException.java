package com.seciii.prism030.common.exception;

import com.seciii.prism030.common.exception.error.ErrorType;
import lombok.Getter;

/**
 * 图数据库业务异常类
 *
 * @author xueruichen
 * @date 2024.04.10
 */
@Getter
public class GraphException extends BaseException{
    public GraphException(ErrorType type) {
        super(type);
    }

    public GraphException(ErrorType type, String message) {
        super(type, message);
    }
}

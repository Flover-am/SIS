package com.seciii.prism063.common.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误类型枚举类
 */
@Getter
@AllArgsConstructor
public enum ErrorType {
    /**
     * 新闻获取相关错误
     */
    NEWS_UNKNOWN_ERROR(300000, "未知错误", 500),
    NEWS_NOT_FOUND(300001, "新闻不存在", 404),
    NEWS_PAGE_OVERFLOW(300002, "新闻页数超限", 400),
    ;
    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    /**
     * http状态码
     */
    private final int httpCode;
}

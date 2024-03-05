package com.seciii.prism063.common.enums;

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
    NEWS_NOT_FOUND(2001, "新闻不存在", 404),
    NEWS_PAGE_NUMBER_OVERFLOW(2002, "页码超出范围", 400),
    NEWS_PAGE_SIZE_OVERFLOW(2003, "页大小超出范围", 400),
    NEWS_LIST_EMPTY(2004, "新闻列表为空", 404),
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

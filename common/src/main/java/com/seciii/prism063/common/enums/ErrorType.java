package com.seciii.prism063.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误类型枚举类
 *
 * @author xueruichen
 * @date 2024.03.05
 */
@Getter
@AllArgsConstructor
public enum ErrorType {
    /**
     * 常见错误类型
     */
    UNKNOWN_ERROR(100000, "Unknown Error", 500),/* 未知错误 */

    BAD_REQUEST(100001, "Bad Request", 400),/* 客户端请求的语法错误 */

    ILLEGAL_ARGUMENTS(100002, "Illegal Arguments", 400),/* 非法参数 */

    UNAUTHORIZED(100003, "Unauthorized", 401),/* 服务端请求要求用户的身份认证 */

    FORBIDDEN(100004, "Forbidden", 403),/* 服务端拒绝执行请求 */

    NOT_FOUND(100005, "Not Found", 404),/* 服务端无法找到客户端请求的资源 */

    SERVER_ERROR(100006, "Server error", 400); /* 服务器内部错误 */

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
    private final int httpStatus;

}

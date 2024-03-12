package com.seciii.prism063.common.exception.error;

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

    SERVER_ERROR(100006, "Server error", 500), /* 服务器内部错误 */

    /**
     * 用户模块错误码
     */
    USER_ALREADY_EXISTS(200000, "User already exists", 400), /* 用户已存在 */

    USER_NOT_EXISTS(200001, "User not exists", 400), /* 用户不存在 */

    PASSWORD_ERROR(200002, "Password error", 400), /* 密码错误 */

    USERNAME_OR_PASSWORD_ERROR(200003, "Username or password error", 400), /* 用户或密码错误 */

    USER_ROLE_NOT_EXISTS(200004, "User role not exists", 500), /* 用户身份不存在 */

    /**
     * 新闻获取相关错误
     */

    NEWS_NOT_FOUND(300000, "新闻不存在", 404);/* 查找的新闻不存在 */


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

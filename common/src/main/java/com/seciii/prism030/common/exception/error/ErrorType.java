package com.seciii.prism030.common.exception.error;

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
     * 新闻相关错误码
     */
    NEWS_NOT_FOUND(300000, "News does not exist", 404), /* 查找的新闻不存在 */

    NEWS_PAGE_OVERFLOW(300001, "News page overflow", 400), /* 新闻页码溢出 */

    NEWS_SEGMENT_SERVICE_UNAVAILABLE(300002, "News segment service unavailable", 500), /* 新闻分词服务不可用 */

    NEWS_INSERT_DUPLICATE(300003, "News insert duplicate", 400), /* 新闻插入重复 */

    NEWS_QUERY_ERROR(300004, "News query error", 400), /* 新闻查询错误 */

    /**
     * 图数据库模块错误码
     */
    LLM_RESULT_ERROR(400000, "LLM result error", 500), /* LLM返回结果错误 */

    GRAPH_GENERATING(400001, "Graph is generating", 400), /* 知识图谱正在生成中 */

    GRAPH_NODE_EXISTS(400002, "Graph node exists", 400), /* 新闻节点已存在 */

    NODE_SAVE_FAILED(400003, "Node save failed", 400); /* 节点存储失败 */

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

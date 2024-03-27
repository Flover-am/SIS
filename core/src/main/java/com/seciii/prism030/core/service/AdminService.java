package com.seciii.prism030.core.service;

/**
 * 管理服务类接口
 *
 * @author xueruichen
 * @date 2024.03.09
 */
@Deprecated
public interface AdminService {
    /**
     * 管理员登陆
     *
     * @param username 管理员用户名
     * @param password 管理员密码
     */
    void login(String username, String password);
}

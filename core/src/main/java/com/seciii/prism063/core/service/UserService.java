package com.seciii.prism063.core.service;

/**
 * 用户服务接口类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
public interface UserService {
    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 用户密码
     */
    void addUser(String username, String password);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     */
    void login(String username, String password);

    void modifyPassword(String oldPassword, String newPassword);
}

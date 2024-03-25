package com.seciii.prism030.core.service;

import com.seciii.prism030.core.enums.RoleType;

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
    void addUser(String username, String password, RoleType role);
    void addUser(String username, String password);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     */
    void login(String username, String password);

    /**
     * 用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void modifyPassword(String oldPassword, String newPassword);
}

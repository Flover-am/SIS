package com.seciii.prism030.core.service;


import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.pojo.vo.user.UserVO;

import java.util.List;

/**
 * 超级管理员服务
 *
 * @author lidongsheng
 * @date 2024.03。25
 */
public interface SuperAdminService {
    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param role 角色类型
     */
    void addUser(String username, String password, RoleType role);

    /**
     * 删除用户
     *
     * @param username 用户名
     */
    void deleteUser(String username);

    /**
     * 获取用户列表
     *
     * @param roleType 用户类型
     * @param pageSize 页大小
     * @param pageOffset 页偏移
     * @return 结果
     */
    List<UserVO> getUsers(RoleType roleType, int pageSize, int pageOffset);

    /**
     * 获取用户数量
     *
     * @param roleType 用户类型
     * @return 用户数量
     */
    Long getUsersCount(RoleType roleType);

    /**
     * 修改用户角色
     *
     * @param username 用户名
     * @param roleType 角色类型
     */
    void modifyRole(String username, RoleType roleType);
}

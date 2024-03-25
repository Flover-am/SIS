package com.seciii.prism030.core.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.common.exception.UserException;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.mapper.auth.RoleMapper;
import com.seciii.prism030.core.mapper.auth.UserMapper;
import com.seciii.prism030.core.mapper.auth.UserRoleMapper;
import com.seciii.prism030.core.pojo.po.auth.UserPO;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import com.seciii.prism030.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 用户服务接口实现类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public void addUser(String username, String password) {
        addUser(username, password, RoleType.USER);
    }

    @Override
    public void addUser(String username, String password, RoleType role) {
        if (role.equals(RoleType.SUPER_ADMIN)) {
            log.warn(String.format("User: %s try to add an admin!", StpUtil.getLoginIdAsLong()));
            throw new UserException(ErrorType.UNAUTHORIZED, "超级管理员不能通过此方法添加");
        }
        // 如果已存在username，则抛出异常
        if (userMapper.existsUser(username) > 0) {
            log.error(String.format("Username: %s already exists.", username));
            throw new UserException(ErrorType.USER_ALREADY_EXISTS, "用户已存在");
        }
        UserPO user = UserPO.builder().username(username).password(BCrypt.hashpw(password, BCrypt.gensalt())).build();
        int result = userMapper.insert(user);
        // 如果数据库添加数据失败，则抛出异常
        if (result <= 0) {
            log.error(String.format("Insert error while adding user: %s.", username));
            throw new UserException(ErrorType.UNKNOWN_ERROR, "数据库添加数据失败");
        }
        // 为用户设定角色
        Long roleId = roleMapper.getRoleIdByName(role.getRoleName());
        UserRolePO userRole = UserRolePO.builder().userId(user.getId()).roleId(roleId).build();
        result = userRoleMapper.insert(userRole);
        // 如果数据库添加数据失败，则抛出异常
        if (result <= 0) {
            log.error(String.format("Insert error while adding user: %s.", username));
            throw new UserException(ErrorType.UNKNOWN_ERROR, "数据库添加数据失败");
        }
    }

    @Override
    public void login(String username, String password) {
        UserPO user = userMapper.getUserByUsername(username);
        // 若用户不存在，抛出异常
        if (user == null) {
            log.error(String.format("User: %s not exist.", username));
            throw new UserException(ErrorType.USER_NOT_EXISTS, "用户不存在");
        }

        // 若密码不匹配，抛出异常
        if (!BCrypt.checkpw(password, user.getPassword())) {
            log.error("Password error.");
            throw new UserException(ErrorType.PASSWORD_ERROR, "密码错误");
        }

        StpUtil.login(user.getId());
    }

    @Override
    public void modifyPassword(String oldPassword, String newPassword) {
        Long id = StpUtil.getLoginIdAsLong();
        UserPO user = userMapper.selectById(id);
        if (user == null) {
            log.error(String.format("User id: %d not exist.", id));
            throw new UserException(ErrorType.USER_NOT_EXISTS, "用户不存在");
        }
        // 若旧密码不正确，抛出异常
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            log.error("Password error.");
            throw new UserException(ErrorType.PASSWORD_ERROR, "密码错误");
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        int result = userMapper.updateById(user);
        if (result <= 0) {
            log.error(String.format("Insert error while changing user password: %d.", id));
            throw new UserException(ErrorType.UNKNOWN_ERROR, "数据库更新数据失败");
        }
    }
}

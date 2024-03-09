package com.seciii.prism063.core.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.user.UserException;
import com.seciii.prism063.core.mapper.auth.RoleMapper;
import com.seciii.prism063.core.mapper.auth.UserMapper;
import com.seciii.prism063.core.pojo.dto.UserDTO;
import com.seciii.prism063.core.pojo.po.auth.RolePO;
import com.seciii.prism063.core.pojo.po.auth.UserPO;
import com.seciii.prism063.core.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理服务实现类
 *
 * @author xueruichen
 * @date 2024.03.09
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public AdminServiceImpl(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public void login(String username, String password) {
        UserDTO user = userMapper.getUserByUsername(username);
        // 若用户不存在，抛出异常
        if (user == null) {
            log.error(String.format("User: %s not exist.", username));
            throw new UserException(ErrorType.USER_NOT_EXISTS, "User not exists");
        }

        // 若密码不匹配，抛出异常
        if (!BCrypt.checkpw(password, user.getPassword())) {
            log.error("Password error.");
            throw new UserException(ErrorType.PASSWORD_ERROR, "Username or password error");
        }

        // 若用户不存在角色，则抛出异常
        List<RolePO> role = roleMapper.getUserRole(user.getId());
        if (role.isEmpty()) {
            log.error(String.format("User: %s role not exist.", username));
            throw new UserException(ErrorType.USER_ROLE_NOT_EXISTS, "User role not exists");
        }

        // 若用户角色为普通用户，则抛出异常
        if ("user".equals(role.get(0).getRoleName())) {
            log.error("Admin try to login through user system.");
            throw new UserException(ErrorType.USERNAME_OR_PASSWORD_ERROR, "User or password error");
        }

        StpUtil.login(user.getId());
    }


}

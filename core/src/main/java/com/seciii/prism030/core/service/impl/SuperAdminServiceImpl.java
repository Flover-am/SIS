package com.seciii.prism030.core.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.exception.UserException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.mapper.auth.RoleMapper;
import com.seciii.prism030.core.mapper.auth.UserMapper;
import com.seciii.prism030.core.mapper.auth.UserRoleMapper;
import com.seciii.prism030.core.pojo.po.auth.RolePO;
import com.seciii.prism030.core.pojo.po.auth.UserPO;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import com.seciii.prism030.core.pojo.vo.user.UserVo;
import com.seciii.prism030.core.service.SuperAdminService;
import com.seciii.prism030.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserService userService;

    public SuperAdminServiceImpl(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userService = userService;
    }


    @Override
    public void addUser(String username, String password, RoleType role) {
        // 检查自己是否是超级管理员
        userService.addUser(username, password, role);
    }

    @Override
    public void deleteUser(String username) {
        // 删除用户
        UserPO user = userMapper.getUserByUsername(username);
        if (user == null) {
            log.error(String.format("User: %s not exist.", username));
            throw new UserException(ErrorType.USER_NOT_EXISTS, "用户不存在");
        }

        userMapper.deleteById(user.getId());
        userMapper.deleteUserRole(user.getId());
    }

    @Override
    public List<UserVo> getUsers(RoleType roleType, int pageSize, int pageOffset) {
        List<UserRolePO> res = userMapper.getUsers(pageSize, pageOffset, roleType == null ? null : roleType.getRoleId());
        return toUserVo(res);
    }


    @Override
    public Long getUsersCount(RoleType roleType) {
        return userMapper.getUsersCount(roleType == null ? null : roleType.getRoleId());
    }

    private List<UserVo> toUserVo(List<UserRolePO> userRolePOS) {
        return userRolePOS.stream().map(
                userRolePO -> UserVo.builder()
                        .username(userMapper.selectById(userRolePO.getUserId()).getUsername())
                        .role(RoleType.getRoleType(userRolePO.getRoleId()).getRoleName())
                        .build()
        ).toList();

    }

    @Override
    public void modifyRole(String username, RoleType roleType) {
        UserPO user = userMapper.getUserByUsername(username);
        if (user == null) {
            log.error(String.format("User: %s not exist.", username));
            throw new UserException(ErrorType.USER_NOT_EXISTS, "用户不存在");
        }
        if (roleType.equals(RoleType.SUPER_ADMIN)) {
            log.error("Can not modify role to super admin.");
            throw new UserException(ErrorType.UNAUTHORIZED, "无法修改为超级管理员");
        }
        userRoleMapper.updateRoleByUserId(user.getId(), roleType.getRoleId());
    }
}

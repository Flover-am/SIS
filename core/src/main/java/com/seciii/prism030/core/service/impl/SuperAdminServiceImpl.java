package com.seciii.prism030.core.service.impl;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.exception.UserException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.mapper.auth.RoleMapper;
import com.seciii.prism030.core.mapper.auth.UserMapper;
import com.seciii.prism030.core.mapper.auth.UserRoleMapper;
import com.seciii.prism030.core.pojo.po.auth.UserPO;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import com.seciii.prism030.core.pojo.vo.user.UserVO;
import com.seciii.prism030.core.service.SuperAdminService;
import com.seciii.prism030.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 超级管理员服务实现类
 *
 * @author lidongsheng
 * @date 2024.03.25
 */
@Service
@Slf4j
@SaCheckRole("super-admin")
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
        StpUtil.checkRole("super-admin");
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

        int res1 = userMapper.deleteById(user.getId());
        if (res1 <= 0) {
            log.error(String.format("Delete user: %s failed.", username));
            throw new UserException(ErrorType.UNKNOWN_ERROR, "删除用户失败");
        }
        userMapper.deleteUserRole(user.getId());
    }

    @Override
    public List<UserVO> getUsers(RoleType roleType, int pageSize, int pageOffset) {
        pageOffset--;
        List<UserRolePO> res = new ArrayList<>();
        if (roleType == null) {
            res = userMapper.getUsers(pageSize, pageOffset);
        } else {
            res = userMapper.getUsersByRoleId(pageSize, pageOffset, roleType.getRoleId());
        }
        for (UserRolePO userRolePO : res) {
            if (userRolePO.getUserId().equals(StpUtil.getLoginIdAsLong())) {
                res.remove(userRolePO);
                break;
            }
        }
        return toUserVo(res);
    }

    @Override
    public Long getUsersCount(RoleType roleType) {
        return userMapper.getUsersCount(roleType == null ? null : roleType.getRoleId());
    }

    private List<UserVO> toUserVo(List<UserRolePO> userRolePOS) {
        return userRolePOS.stream().map(
                userRolePO -> UserVO.builder()
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
        if (user.getId().equals(StpUtil.getLoginIdAsLong())) {
            log.error("Can not modify role to self.");
            throw new UserException(ErrorType.FORBIDDEN, "无法修改自己的角色");
        }
        if (roleType.equals(RoleType.SUPER_ADMIN)) {
            log.error("Can not modify role to super admin.");
            throw new UserException(ErrorType.FORBIDDEN, "无法修改为超级管理员");
        }
        userRoleMapper.updateRoleByUserId(user.getId(), roleType.getRoleId());
    }
}

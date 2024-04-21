package com.seciii.prism030.core.service.impl;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.exception.UserException;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.mapper.auth.PermissionMapper;
import com.seciii.prism030.core.mapper.auth.RoleMapper;
import com.seciii.prism030.core.mapper.auth.UserMapper;
import com.seciii.prism030.core.mapper.auth.UserRoleMapper;
import com.seciii.prism030.core.pojo.po.auth.RolePO;
import com.seciii.prism030.core.pojo.po.auth.UserPO;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

/**
 * @author lidongsheng
 * @date 2021.03.10
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SuperAdminServiceImplTest {

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserRoleMapper userRoleMapper;
    @MockBean
    private RoleMapper roleMapper;
    @MockBean
    private PermissionMapper permissionMapper;

    @Autowired
    @InjectMocks
    private SuperAdminServiceImpl superAdminService;

    @BeforeEach
    void beforeEach() {
        UserPO admin = UserPO.builder().id(1L).username("admin")
                .password(BCrypt.hashpw("admin123", BCrypt.gensalt()))
                .build();
        UserPO existUser = UserPO.builder().id(3L).username("existUser")
                .password(BCrypt.hashpw("existUserPasswd", BCrypt.gensalt()))
                .build();
        UserPO newUser = UserPO.builder().id(4L).username("newUser")
                .password(BCrypt.hashpw("newUserPasswd", BCrypt.gensalt()))
                .build();


        RolePO adminRole = RolePO.builder().id(1L).roleName(RoleType.SUPER_ADMIN.getRoleName()).build();
        RolePO userRole = RolePO.builder().id(3L).roleName(RoleType.USER.getRoleName()).build();


        // 根据用户名获取存在用户
        Mockito.when(userMapper.getUserByUsername(existUser.getUsername())).thenReturn(existUser);
        // 根据用户名获取不存在用户
        Mockito.when(userMapper.getUserByUsername(newUser.getUsername())).thenReturn(null);
        Mockito.when(userMapper.getUserByUsername(Mockito.argThat(argument -> !"existUser".equals(argument) && !"newUser".equals(argument))))
                .thenThrow(UserException.class);

        Mockito.when(userMapper.deleteById(existUser.getId())).thenReturn(1);
        // 删除UserRole
        Mockito.doNothing().when(userMapper).deleteUserRole(existUser.getId());

        // 获取用户列表
        Mockito.when(userMapper.getUsers(1, 0)).thenReturn(List.of(UserRolePO.builder().userId(1L).roleId(1L).build()));
        Mockito.when(userMapper.getUsers(1, 0)).thenReturn(List.of(UserRolePO.builder().userId(1L).roleId(1L).build()));
        Mockito.when(userMapper.getUsersByRoleId(1, 0, null)).thenReturn(List.of(UserRolePO.builder().userId(1L).roleId(1L).build()));

        // 获取用户数量
        Mockito.when(userMapper.getUsersCount(null)).thenReturn(1L);
        Mockito.when(userMapper.getUsersCount(RoleType.USER.getRoleId())).thenReturn(30L);


        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && existUser.getUsername().equals(argument.getUsername()))))
                .thenThrow(UserException.class);
        // 测试添加用户
        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && newUser.getUsername().equals(argument.getUsername()))))
                .thenReturn(1);

        Mockito.when(userMapper.updateById(Mockito.argThat(
                        argument -> argument != null && "admin".equals(argument.getUsername()) || "张三".equals(argument.getUsername()))))
                .thenReturn(1);

        Mockito.when(userMapper.selectById(1L)).thenReturn(admin);
        Mockito.when(userMapper.selectById(Mockito.argThat(
                        argument -> argument != null && 1L != (Long) argument && 2L != (Long) argument)))
                .thenThrow(UserException.class);

        Mockito.when(userRoleMapper.insert(Mockito.any(UserRolePO.class))).thenReturn(1);

        Mockito.when(roleMapper.getUserRole(1L)).thenReturn(List.of(adminRole));
        Mockito.when(roleMapper.getUserRole(Mockito.argThat(argument -> argument != null && argument != 1L))).thenReturn(List.of(userRole));
        // 测试获取用户角色(Name2Id)
        Mockito.when(roleMapper.getRoleIdByName(RoleType.SUPER_ADMIN.getRoleName())).thenReturn(1L);
        Mockito.when(roleMapper.getRoleIdByName(RoleType.NEWS_ADMIN.getRoleName())).thenReturn(2L);
        Mockito.when(roleMapper.getRoleIdByName(RoleType.USER.getRoleName())).thenReturn(3L);
        Mockito.when(roleMapper.getRoleIdByName(Mockito.any(String.class))).thenReturn(null);

        Mockito.when(userRoleMapper.insert(Mockito.any(UserRolePO.class))).thenReturn(1);
        Mockito.when(userRoleMapper.getUserRoleByUserId(Mockito.any(Long.class))).thenReturn(UserRolePO.builder().roleId(3L).build());

        Mockito.when(permissionMapper.getUserPermission(Mockito.any(Long.class))).thenReturn(List.of());
    }

    @Test
    void addUserTest() {
        // 没登录
        // 测试添加重复用户
        StpUtil.logout();
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("existUser", "existUserPasswd", RoleType.USER));
        // 测试添加用户
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("newUser", "newUserPasswd", RoleType.USER));
        // 测试添加超级管理员
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("admin", "admin123", RoleType.USER));

        StpUtil.login(2L);
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("existUser", "existUserPasswd", RoleType.USER));
        // 测试添加用户
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("newUser", "newUserPasswd", RoleType.USER));
        // 测试添加超级管理员
        Assertions.assertThrows(SaTokenException.class, () -> superAdminService.addUser("admin", "admin123", RoleType.USER));

        StpUtil.login(1L);
        Assertions.assertThrows(UserException.class, () -> superAdminService.addUser("existUser", "existUserPasswd", RoleType.USER));
        // 测试添加用户
        Assertions.assertDoesNotThrow(() -> superAdminService.addUser("newUser", "newUserPasswd", RoleType.USER));
        // 测试添加超级管理员
        Assertions.assertThrows(UserException.class, () -> superAdminService.addUser("admin", "admin123", RoleType.USER));
    }

    @Test
    void deleteUserTest() {
        // 测试删除不存在用户
        Assertions.assertThrows(UserException.class, () -> superAdminService.deleteUser("newUser"));
        // 测试删除存在用户
        Assertions.assertDoesNotThrow(() -> superAdminService.deleteUser("existUser"));
    }

    @Test
    void getUsersTest() {
        // 测试获取用户列表
        Assertions.assertEquals(1, superAdminService.getUsers(null, 1, 1).size());
        // 测试获取用户数量
        Assertions.assertEquals(1, superAdminService.getUsersCount(null));
        Assertions.assertEquals(30, superAdminService.getUsersCount(RoleType.USER));
    }

    @Test
    void modifyRoleTest() {
        // 测试修改不存在用户角色
        Assertions.assertThrows(UserException.class, () -> superAdminService.modifyRole("newUser", RoleType.USER));
        // 修改为超级管理员
        Assertions.assertThrows(UserException.class, () -> superAdminService.modifyRole("existUser", RoleType.SUPER_ADMIN));
        // 测试修改存在用户角色
        Assertions.assertDoesNotThrow(() -> superAdminService.modifyRole("existUser", RoleType.USER));
    }
}
package com.seciii.prism030.core.service.impl;

import cn.dev33.satoken.annotation.SaCheckRole;
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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

/**
 * 用户服务测试类
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {
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
    private UserServiceImpl userService;

    @BeforeEach
    void beforeEach() {
        UserPO admin = UserPO.builder().id(1L).username("admin")
                .password(BCrypt.hashpw("admin123", BCrypt.gensalt()))
                .build();
        UserPO user = UserPO.builder().id(2L).username("张三")
                .password(BCrypt.hashpw("123abc", BCrypt.gensalt()))
                .build();

        UserPO existUser = UserPO.builder().id(3L).username("existUser")
                .password(BCrypt.hashpw("existUserPasswd", BCrypt.gensalt()))
                .build();
        UserPO newUser = UserPO.builder().id(4L).username("newUser")
                .password(BCrypt.hashpw("newUserPasswd", BCrypt.gensalt()))
                .build();

        RolePO adminRole = RolePO.builder().id(1L).roleName(RoleType.SUPER_ADMIN.getRoleName()).build();
        RolePO userRole = RolePO.builder().id(3L).roleName(RoleType.USER.getRoleName()).build();

        Mockito.when(userMapper.getUserByUsername("admin")).thenReturn(admin);
        Mockito.when(userMapper.getUserByUsername("张三")).thenReturn(user);

        Mockito.when(userMapper.existsUser(existUser.getUsername())).thenReturn(1);
        Mockito.when(userMapper.existsUser(newUser.getUsername())).thenReturn(-1);
        Mockito.when(userMapper.existsUser("admin")).thenReturn(1);
        Mockito.when(userMapper.existsUser("张三")).thenReturn(2);


        Mockito.when(userMapper.insert(null)).thenReturn(-1);
        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && !"admin".equals(argument.getUsername()) && !"张三".equals(argument.getUsername()))))
                .thenReturn(1);
        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && ("admin".equals(argument.getUsername()) || "张三".equals(argument.getUsername())))))
                .thenThrow(UserException.class);
        // 测试添加重复用户
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
        Mockito.when(userMapper.selectById(2L)).thenReturn(user);
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

    @AfterEach
    void afterEach() {
        Mockito.reset(userMapper, userRoleMapper, roleMapper);
    }

    /**
     * 添加用户接口测试
     */
    @Test
    void addUserTest() {
        // 测试添加重复用户
        Assertions.assertThrows(UserException.class, () -> userService
                .addUser("existUser", "existUserPasswd"));
        // 测试添加用户
        Assertions.assertDoesNotThrow(() -> userService
                .addUser("newUser", "newUserPasswd"));
        // 测试添加超级管理员
        Assertions.assertThrows(UserException.class, () -> userService
                .addUser("admin", "admin123"));
    }

    /**
     * 登陆接口测试
     */
    @Test
    void loginTest() {
        // 测试用户不存在
        Assertions.assertThrows(UserException.class, () -> userService.login("牢大", "laoda123"));
        // 测试密码错误
        Assertions.assertThrows(UserException.class, () -> userService.login("张三", "123abcd"));
        // 测试登陆成功
        Assertions.assertDoesNotThrow(() -> userService.login("张三", "123abc"));
        Assertions.assertTrue(StpUtil.isLogin());
        StpUtil.logout();
    }

    /**
     * 添加用户接口测试
     */
    @Test
    void addUserTest2() {
        // 测试添加重复用户
        Assertions.assertThrows(UserException.class, () -> userService.addUser("admin", "123abc"));
        Assertions.assertThrows(UserException.class, () -> userService.addUser("张三", "123abc"));
        // 测试添加用户
        Assertions.assertDoesNotThrow(() -> userService.addUser("老八", "123xiaohanbao"));
    }


    /**
     * 修改密码接口测试
     */
    @Test
    void modifyPasswordTest() {
        // 测试未登陆修改密码
        Assertions.assertThrows(SaTokenException.class, () -> userService.modifyPassword("123abc", "1234abc"));
        // 测试已登陆修改密码失败
        StpUtil.login(1L);
        Assertions.assertThrows(UserException.class, () -> userService.modifyPassword("admin1234", "admin123"));
        // 测试修改密码成功
        Assertions.assertDoesNotThrow(() -> userService.modifyPassword("admin123", "admin1234"));
        StpUtil.logout();
    }
}

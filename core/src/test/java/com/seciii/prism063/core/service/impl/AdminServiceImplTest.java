package com.seciii.prism063.core.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism063.common.exception.UserException;
import com.seciii.prism063.core.enums.RoleType;
import com.seciii.prism063.core.mapper.auth.PermissionMapper;
import com.seciii.prism063.core.mapper.auth.RoleMapper;
import com.seciii.prism063.core.mapper.auth.UserMapper;
import com.seciii.prism063.core.mapper.auth.UserRoleMapper;
import com.seciii.prism063.core.pojo.po.auth.RolePO;
import com.seciii.prism063.core.pojo.po.auth.UserPO;
import com.seciii.prism063.core.pojo.po.auth.UserRolePO;
import org.junit.jupiter.api.AfterEach;
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
 * 管理员服务测试类
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AdminServiceImplTest {
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
    private AdminServiceImpl adminService;

    @BeforeEach
    void beforeEach() {
        UserPO admin = UserPO.builder().id(1L).username("admin")
                .password(BCrypt.hashpw("admin123", BCrypt.gensalt()))
                .build();
        UserPO user = UserPO.builder().id(2L).username("张三")
                .password(BCrypt.hashpw("123abc", BCrypt.gensalt()))
                .build();
        RolePO adminRole = RolePO.builder().id(1L).roleName(RoleType.SUPER_ADMIN.getRoleName()).build();
        RolePO userRole = RolePO.builder().id(3L).roleName(RoleType.USER.getRoleName()).build();

        Mockito.when(userMapper.getUserByUsername("admin")).thenReturn(admin);
        Mockito.when(userMapper.getUserByUsername("张三")).thenReturn(user);

        Mockito.when(userMapper.existsUser("admin")).thenReturn(1);
        Mockito.when(userMapper.existsUser("张三")).thenReturn(2);

        Mockito.when(userMapper.insert(null)).thenReturn(-1);
        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && !"admin".equals(argument.getUsername()) && !"张三".equals(argument.getUsername()))))
                .thenReturn(1);
        Mockito.when(userMapper.insert(Mockito.argThat(
                        argument -> argument != null && ("admin".equals(argument.getUsername()) || "张三".equals(argument.getUsername())))))
                .thenThrow(UserException.class);
        Mockito.when(userMapper.updateById(Mockito.argThat(
                        argument -> argument != null && "admin".equals(argument.getUsername()) || "张三".equals(argument.getUsername()))))
                .thenReturn(1);

        Mockito.when(userMapper.selectById(1L)).thenReturn(admin);
        Mockito.when(userMapper.selectById(2L)).thenReturn(user);
        Mockito.when(userMapper.selectById(Mockito.argThat(
                        argument -> argument != null && 1L != (Long)argument && 2L != (Long) argument)))
                .thenThrow(UserException.class);

        Mockito.when(userRoleMapper.insert(Mockito.any(UserRolePO.class))).thenReturn(1);

        Mockito.when(roleMapper.getUserRole(1L)).thenReturn(List.of(adminRole));
        Mockito.when(roleMapper.getUserRole(Mockito.argThat(argument -> argument != null && argument != 1L))).thenReturn(List.of(userRole));

        Mockito.when(permissionMapper.getUserPermission(Mockito.any(Long.class))).thenReturn(List.of());
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(userMapper, userRoleMapper, roleMapper);
    }

    /**
     * 登陆接口测试
     */
    @Test
    void loginTest() {
        // 测试用户不存在
        Assertions.assertThrows(UserException.class, () -> adminService.login("老八", "laoba123"));
        // 测试用户密码错误
        Assertions.assertThrows(UserException.class, () -> adminService.login("admin", "admin12345"));
        // 测试普通用户登陆失败
        Assertions.assertThrows(UserException.class, () -> adminService.login("张三", "123abc"));
        // 测试登陆成功
        Assertions.assertDoesNotThrow(() -> adminService.login("admin", "admin123"));
        Assertions.assertTrue(StpUtil.isLogin());
    }
}

package com.seciii.prism063.core;

import com.seciii.prism063.core.mapper.auth.PermissionMapper;
import com.seciii.prism063.core.mapper.auth.RoleMapper;
import com.seciii.prism063.core.mapper.auth.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Prism063ApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        System.out.println(roleMapper.getUserRole(2L));
        System.out.println(permissionMapper.getUserPermission(2L));
    }

}

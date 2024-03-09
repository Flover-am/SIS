package com.seciii.prism063.core.utils;

import cn.dev33.satoken.stp.StpInterface;
import com.seciii.prism063.core.mapper.auth.PermissionMapper;
import com.seciii.prism063.core.mapper.auth.RoleMapper;
import com.seciii.prism063.core.pojo.po.auth.PermissionPO;
import com.seciii.prism063.core.pojo.po.auth.RolePO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色权限获取工具类
 *
 * @author xueruichen
 * @date 2024.03.09
 */
@Component
public class StpInterfaceUtil implements StpInterface {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public StpInterfaceUtil(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        List<PermissionPO> permission = permissionMapper.getUserPermission((Long) o);
        return permission.stream().map(PermissionPO::getPermissionName).toList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        List<RolePO> role = roleMapper.getUserRole((Long) o);
         return role.stream().map(RolePO::getRoleName).toList();
    }
}

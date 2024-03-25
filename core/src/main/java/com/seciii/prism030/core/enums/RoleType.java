package com.seciii.prism030.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@Getter
public enum RoleType {

    SUPER_ADMIN("super-admin", 1L), /* 超级管理员 */

    NEWS_ADMIN("news-admin", 2L),   /* 新闻管理员 */

    USER("user", 3L);               /* 普通用户 */

    /**
     * 角色名
     */
    private final String roleName;
    private final Long roleId;

    RoleType(String roleName, Long roleId) {
        this.roleName = roleName;
        this.roleId = roleId;
    }

    // 根据角色名获取角色
    public static RoleType getRoleType(String roleName) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getRoleName().equals(roleName)) {
                return roleType;
            }
        }
        return null;
    }

    public static RoleType getRoleType(Long roleId) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getRoleId().equals(roleId)) {
                return roleType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return roleName;
    }
}

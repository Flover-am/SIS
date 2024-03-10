package com.seciii.prism063.core.enums;

/**
 * 用户角色枚举
 *
 * @author xueruichen
 * @date 2024.03.10
 */
public enum RoleType {
    SUPER_ADMIN("super-admin"), /* 超级管理员 */
    NEWS_ADMIN("news-admin"),   /* 新闻管理员 */
    USER("user");               /* 普通用户 */

    /**
     * 角色名
     */
    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

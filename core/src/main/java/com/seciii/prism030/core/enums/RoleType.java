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

}

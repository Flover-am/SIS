package com.seciii.prism063.core.pojo.po.auth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限关系PO类
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Data
public class RolePermissionPO {
    /**
     * 角色权限关系id
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

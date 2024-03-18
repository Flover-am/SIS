package com.seciii.prism030.core.pojo.po.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限关系PO类
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Data
@Builder
@TableName(schema = "prism030", value = "t_auth_role_permission")
public class RolePermissionPO {
    /**
     * 角色权限关系id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime updateTime;
}

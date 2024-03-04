package com.seciii.prism063.core.pojo.po.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限PO类
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Data
@Builder
@TableName("t_auth_permission")
public class PermissionPO {
    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

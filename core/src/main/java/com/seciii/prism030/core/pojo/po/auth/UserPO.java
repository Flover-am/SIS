package com.seciii.prism030.core.pojo.po.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户PO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@Builder
@TableName(schema = "prism030", value = "t_auth_user")
public class UserPO {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

package com.seciii.prism063.core.pojo.po.auth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色关系PO类
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Data
public class UserRolePO {
    /**
     * 用户角色关系id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

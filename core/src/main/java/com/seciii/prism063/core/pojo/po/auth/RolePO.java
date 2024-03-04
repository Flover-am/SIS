package com.seciii.prism063.core.pojo.po.auth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色PO类
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Data
public class RolePO {
    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

package com.seciii.prism063.core.pojo.po.auth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户PO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
public class UserPO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

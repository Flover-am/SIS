package com.seciii.prism063.core.pojo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 用户DTO类
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@Data
@Builder
public class UserDTO {
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
}

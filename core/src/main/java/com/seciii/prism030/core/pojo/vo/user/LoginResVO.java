package com.seciii.prism030.core.pojo.vo.user;

import lombok.Builder;
import lombok.Data;

/**
 * 登录后返回token和角色
 * @date 2024.03.25
 * @author lidongsheng
 */
@Data
@Builder
public class LoginResVO {
    private final String token;
    private final String role;
    public LoginResVO(String token, String role) {
        this.token = token;
        this.role = role;
    }
}

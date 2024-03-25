package com.seciii.prism030.core.pojo.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LoginResVo {
    private final String token;
    private final String role;

    public LoginResVo(String token, String role) {
        this.token = token;
        this.role = role;
    }
}

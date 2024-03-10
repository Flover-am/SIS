package com.seciii.prism063.core.pojo.vo.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 注册VO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@Builder
public class RegisterVO {
    /**
     * 用户名
     */
    @NotNull
    @Size(min = 1, max = 20, message = "用户名不能长于20位")
    private String username;

    /**
     * 用户密码
     */
    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$", message = "密码必须同时包含数字和字母")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位")
    private String password;
}

package com.seciii.prism063.core.pojo.vo.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 登陆VO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@Builder
public class LoginVO {
    /**
     * 用户名
     */
    @NotNull
    @Size(min = 1, max = 20, message = "请输入正确格式的用户名")
    private String username;

    /**
     * 用户密码
     */
    @NotNull
    @Size(min = 6, max = 20, message = "请输入正确格式的密码")
    private String password;
}

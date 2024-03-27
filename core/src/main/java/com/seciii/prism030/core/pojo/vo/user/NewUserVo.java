package com.seciii.prism030.core.pojo.vo.user;

import com.seciii.prism030.core.enums.RoleType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * 新建用户VO类
 * @date 2024.03.25
 * @author lidongsheng
 */
@Data
@Builder
public class NewUserVo {
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
    @Pattern(regexp = "^(\\s*|[0-9A-Za-z]*)$", message = "密码必须同时包含数字和字母")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位")
    private String password;
    /**
     * 用户角色
     */
    @NotNull
    @Pattern(regexp = "super-admin|admin|user", message = "请输入正确格式的角色")
    private String role;
}

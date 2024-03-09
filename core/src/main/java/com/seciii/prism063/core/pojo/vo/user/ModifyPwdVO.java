package com.seciii.prism063.core.pojo.vo.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户修改密码VO类
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@Data
@Builder
public class ModifyPwdVO {
    /**
     * 旧密码
     */
    @NotNull
    @Size(min = 6, max = 20, message = "请输入正确格式的密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotNull
    @Pattern(regexp = "^(\\s*|[0-9A-Za-z]*)$", message = "密码必须同时包含数字和字母")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位")
    private String newPassword;
}

package com.seciii.prism030.core.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.user.LoginVO;
import com.seciii.prism030.core.pojo.vo.user.ModifyPwdVO;
import com.seciii.prism030.core.service.AdminService;
import com.seciii.prism030.core.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * B端管理控制器类
 *
 * @author xueruichen
 * @date 2024.03.09
 */
@RestController
@RequestMapping("/v1")
@Deprecated
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    /**
     * 管理员登陆
     *
     * @param loginVO 用户登陆VO
     * @return 用户token
     */
    @PostMapping("/admin/login")
    public Result<String> login(@Validated LoginVO loginVO) {
        adminService.login(loginVO.getUsername(), loginVO.getPassword());
        return Result.success(StpUtil.getTokenInfo().tokenValue);
    }

    /**
     * 管理员修改密码
     *
     * @param modifyPwdVO 修改密码VO
     * @return 响应结果
     */
    @PutMapping("/admin/password")
    public Result<Void> modifyPassword(@Validated ModifyPwdVO modifyPwdVO) {
        userService.modifyPassword(modifyPwdVO.getOldPassword(), modifyPwdVO.getNewPassword());
        return Result.success();
    }
}

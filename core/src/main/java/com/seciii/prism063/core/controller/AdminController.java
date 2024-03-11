package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.vo.user.LoginVO;
import com.seciii.prism063.core.pojo.vo.user.ModifyPwdVO;
import com.seciii.prism063.core.service.AdminService;
import com.seciii.prism063.core.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B端管理控制器类
 *
 * @author xueruichen
 * @date 2024.03.09
 */
@RestController
@RequestMapping("/v1")
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
     * @return 响应结果
     */
    @GetMapping("/admin/login")
    public Result<Void> login(@Validated LoginVO loginVO) {
        adminService.login(loginVO.getUsername(), loginVO.getPassword());
        return Result.success();
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

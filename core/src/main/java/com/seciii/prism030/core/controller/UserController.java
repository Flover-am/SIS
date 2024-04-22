package com.seciii.prism030.core.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.core.pojo.vo.user.LoginResVO;
import com.seciii.prism030.core.pojo.vo.user.LoginVO;
import com.seciii.prism030.core.pojo.vo.user.RegisterVO;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.user.ModifyPwdVO;
import com.seciii.prism030.core.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@RestController
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     *
     * @param registerVO 用户注册VO
     * @return 响应结果
     */
    @PostMapping("/user/register")
    public Result<Void> register(@Validated RegisterVO registerVO) {
        userService.addUser(registerVO.getUsername(), registerVO.getPassword());
        return Result.success();
    }

    /**
     * 用户登陆
     *
     * @param loginVO 用户登陆VO
     * @return 返回token
     */
    @PostMapping("/user/login")
    public Result<LoginResVO> login(@Validated LoginVO loginVO) {
        String roleName = userService.login(loginVO.getUsername(), loginVO.getPassword()).getRoleName();
        return Result.success(new LoginResVO(StpUtil.getTokenValue(), roleName));
    }

    /**
     * 用户修改密码
     *
     * @param modifyPwdVO 修改密码VO
     * @return 响应结果
     */
    @PutMapping("/user/password")
    public Result<Void> modifyPassword(@Validated ModifyPwdVO modifyPwdVO) {
        userService.modifyPassword(modifyPwdVO.getOldPassword(), modifyPwdVO.getNewPassword());
        return Result.success();
    }
}

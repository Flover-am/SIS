package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 响应结果
     */
    @PostMapping("/user/register")
    public Result<Void> register(@RequestParam String username, @RequestParam String password) {
        userService.addUser(username, password);
        return Result.success();
    }

    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 响应结果
     */
    @GetMapping("/user/login")
    public Result<Void> login(@RequestParam String username, @RequestParam String password) {
        userService.login(username, password);
        return Result.success();
    }
}

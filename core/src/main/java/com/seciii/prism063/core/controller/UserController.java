package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@RestController
public class UserController {

    @PostMapping("/user/register")
    public Result<Void> register() {
        return Result.success();
    }

    @GetMapping("/user/login")
    public Result<Void> login() {
        return Result.success();
    }
}

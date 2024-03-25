package com.seciii.prism030.core.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.pojo.vo.user.LoginVO;
import com.seciii.prism030.core.pojo.vo.user.NewUserVo;
import com.seciii.prism030.core.pojo.vo.user.UserVo;
import com.seciii.prism030.core.service.SuperAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author : lidongsheng
 * @date 2024.03.25
 */
@RestController
@RequestMapping("/v1")
public class SuperAdminController {

    SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }



    @PostMapping("/superAdmin/addUser")
    public Result<Void> addUser(@Validated NewUserVo newUserVo) {
        superAdminService.addUser(newUserVo.getUsername(), newUserVo.getPassword(), RoleType.getRoleType(newUserVo.getRole()));
        return Result.success();
    }

    @GetMapping("/superAdmin/users")
    public Result<List<UserVo>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam int pageSize,
            @RequestParam int pageOffset) {
        List<UserVo> res = superAdminService.getUsers(RoleType.getRoleType(role), pageSize, pageOffset);
        return Result.success(res);
    }

    @PutMapping("/superAdmin/deleteUser")
    public Result<Void> deleteUser(@RequestParam String username) {
        superAdminService.deleteUser(username);
        return Result.success();
    }
    @GetMapping("/superAdmin/usersCount")
    public Result<Long> getUserCount(@RequestParam(required = false) String role) {
        return Result.success(superAdminService.getUsersCount(RoleType.getRoleType(role)));
    }

}

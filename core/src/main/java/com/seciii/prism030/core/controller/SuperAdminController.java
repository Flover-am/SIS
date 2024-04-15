package com.seciii.prism030.core.controller;

import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.pojo.vo.user.NewUserVO;
import com.seciii.prism030.core.pojo.vo.user.UserVO;
import com.seciii.prism030.core.pojo.vo.user.UserListVO;
import com.seciii.prism030.core.service.SuperAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author : lidongsheng
 * @date 2024.03.27
 */
@RestController
@RequestMapping("/v1")
public class SuperAdminController {

    SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }


    /**
     * 添加用户
     * @param newUserVo 新增用户信息
     * @date 2024.03.27
     */
    @PostMapping("/superAdmin/addUser")
    public Result<Void> addUser(@Validated NewUserVO newUserVo) {
        superAdminService.addUser(newUserVo.getUsername(), newUserVo.getPassword(), RoleType.getRoleType(newUserVo.getRole()));
        return Result.success();
    }

    /**
     * 获取用户列表
     * @param role 用户角色
     * @param pageSize 页面大小
     * @param pageOffset 页面偏移量
     * @return 用户列表
     * @date 2024.03.27
     */
    @GetMapping("/superAdmin/users")
    public Result<UserListVO> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam int pageSize,
            @RequestParam int pageOffset) {
        RoleType roleType;
        if (role == null) {
            roleType = null;
        } else {
            roleType = RoleType.getRoleType(role);
        }
        List<UserVO> res = superAdminService.getUsers(roleType, pageSize, pageOffset);
        long count = superAdminService.getUsersCount(roleType);
        return Result.success(new UserListVO(count, res));
    }

    /**
     * 删除用户
     * @param username 用户名
     * @date 2024.03.27
     */
    @DeleteMapping("/superAdmin/deleteUser")
    public Result<Void> deleteUser(@RequestParam String username) {
        superAdminService.deleteUser(username);
        return Result.success();
    }

    /**
     * 获取用户数量
     * @param role 用户角色
     * @return 用户数量
     * @date 2024.03.27
     */
    @GetMapping("/superAdmin/usersCount")
    public Result<Long> getUserCount(@RequestParam(required = false) String role) {
        return Result.success(superAdminService.getUsersCount(RoleType.getRoleType(role)));
    }

    /**
     * 修改用户角色
     * @param username 用户名
     * @param role 用户角色
     * @date 2024.03.27
     */
    @PostMapping("/superAdmin/modifyRole")
    public Result<Void> modifyRole(@RequestParam String username, @RequestParam String role) {
        superAdminService.modifyRole(username, RoleType.getRoleType(role));
        return Result.success();
    }

}

package com.seciii.prism030.core.service;


import com.seciii.prism030.core.enums.RoleType;
import com.seciii.prism030.core.pojo.vo.user.UserVo;

import java.util.List;

public interface SuperAdminService {


    void addUser(String username, String password, RoleType role);


    void deleteUser(String username);

    List<UserVo> getUsers(RoleType roleType, int pageSize, int pageOffset);

    Long getUsersCount(RoleType roleType);

    void modifyRole(String username, RoleType roleType);
}

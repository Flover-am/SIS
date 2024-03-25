package com.seciii.prism030.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism030.core.pojo.po.auth.UserPO;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return
     */
    int existsUser(String username);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户数据
     */
    UserPO getUserByUsername(String username);

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */

    List<UserRolePO> getUsers(
            @Param("pageSize") int pageSize,
            @Param("pageOffset") int pageOffset,
            @Param("roleId") Long roleId
    );

    Long getUsersCount(
            @Param("roleId") Long roleId
    );

    void deleteUserRole(
            @Param("userId") Long userId
    );


}

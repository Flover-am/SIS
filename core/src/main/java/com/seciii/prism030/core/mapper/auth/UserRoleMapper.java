package com.seciii.prism030.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism030.core.pojo.po.auth.UserRolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色Mapper接口
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {
    /**
     * 根据用户ID查询用户角色
     */
    UserRolePO getUserRoleByUserId(
            @Param("userId") Long userId);

    void updateRoleByUserId(Long userId, Long roleId);
}

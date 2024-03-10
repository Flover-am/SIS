package com.seciii.prism063.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.auth.RolePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {
    /**
     * 根据用户id获取用户角色
     *
     * @param userId 用户id
     * @return 用户角色名
     */
    List<RolePO> getUserRole(Long userId);

    Long getRoleIdByName(String roleName);
}

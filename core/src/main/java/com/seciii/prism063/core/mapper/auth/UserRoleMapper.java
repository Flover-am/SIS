package com.seciii.prism063.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.auth.UserRolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色Mapper接口
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {
}

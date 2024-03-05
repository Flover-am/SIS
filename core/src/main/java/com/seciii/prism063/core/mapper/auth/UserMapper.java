package com.seciii.prism063.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.auth.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    int existsUser(String username);
}

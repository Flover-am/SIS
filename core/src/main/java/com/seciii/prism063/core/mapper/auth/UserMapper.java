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
}

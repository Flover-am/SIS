package com.seciii.prism063.core.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.user.UserException;
import com.seciii.prism063.core.mapper.auth.UserMapper;
import com.seciii.prism063.core.pojo.po.auth.UserPO;
import com.seciii.prism063.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 用户服务接口实现类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void addUser(String username, String password) {
        // 如果已存在username，则抛出异常
        if (userMapper.existsUser(username) > 0) {
            log.error(String.format("Username: %s already exists.", username));
            throw new UserException(ErrorType.ILLEGAL_ARGUMENTS, "User already exists");
        }
        UserPO user = UserPO.builder().username(username).password(BCrypt.hashpw(password, BCrypt.gensalt())).build();
        int result = userMapper.insert(user);
        // 如果数据库添加数据失败，则抛出异常
        if (result <= 0) {
            log.error(String.format("Insert error while adding user: %s.", username));
            throw new UserException(ErrorType.UNKNOWN_ERROR, "Insert error");
        }

    }

    @Override
    public void login(String username, String password) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getUsername, username);
        UserPO user = userMapper.selectOne(wrapper);
        // 若用户不存在，抛出异常
        if (user == null) {
            log.error(String.format("User: %s not exist.", username));
            throw new UserException(ErrorType.ILLEGAL_ARGUMENTS, "User not exists");
        }
        // 若密码不匹配，抛出异常
        if (!BCrypt.checkpw(password, user.getPassword())) {
            log.error("Password error.");
            throw new UserException(ErrorType.BAD_REQUEST, "Password error");
        } else {
            StpUtil.login(user.getId());
        }
    }
}

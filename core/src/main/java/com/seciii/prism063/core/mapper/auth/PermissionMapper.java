package com.seciii.prism063.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.auth.PermissionPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 权限Mapper接口
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPO> {
    /**
     * 获取用户权限
     *
     * @param userId 用户id
     * @return
     */
    List<PermissionPO> getUserPermission(Long userId);
}

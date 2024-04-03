package com.seciii.prism030.core.pojo.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * VO for users
 * @date 2024.03.25
 * @author lidongsheng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListVO {
    /**
     * total count of users
     */
    public long totalCount;
    /**
     * list of users
     */
    public List<UserVO> usersList;
}

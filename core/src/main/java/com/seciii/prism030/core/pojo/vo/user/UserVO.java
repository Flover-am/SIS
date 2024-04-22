package com.seciii.prism030.core.pojo.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * VO for user
 * @date 2024.03.25
 * @author lidongsheng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String username;
    private String role;

}

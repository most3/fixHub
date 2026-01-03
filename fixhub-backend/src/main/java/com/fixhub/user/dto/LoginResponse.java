package com.fixhub.user.dto;

import com.fixhub.user.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录成功后返回给前端的简要信息。
 */
@Data
@NoArgsConstructor
public class LoginResponse {

    private Long userId;
    private String username;
    private UserRole role;
    private String message;

    public static LoginResponse success(Long userId, String username, UserRole role) {
        LoginResponse response = new LoginResponse();
        response.setUserId(userId);
        response.setUsername(username);
        response.setRole(role);
        response.setMessage("登录成功");
        return response;
    }

}

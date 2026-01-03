package com.fixhub.user.dto;

import com.fixhub.user.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求体。
 */
@Data
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度需在 3 到 50 个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只允许字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度需在 6 到 100 个字符之间")
    private String password;

    @Size(max = 100)
    private String displayName;

    private UserRole role;

}

package com.fixhub.user.controller;

import com.fixhub.common.response.Result;
import com.fixhub.user.dto.RegisterUserRequest;
import com.fixhub.user.dto.UserResponse;
import com.fixhub.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 创建新用户（管理员接口）。
     * TODO: 添加权限校验，仅允许管理员调用
     */
    @PostMapping
    public Result<UserResponse> createUser(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = userService.createUser(request);
        return Result.success(response);
    }
}

package com.fixhub.user.controller;

import com.fixhub.user.dto.LoginRequest;
import com.fixhub.user.dto.LoginResponse;
import com.fixhub.user.dto.RegisterUserRequest;
import com.fixhub.user.dto.UserResponse;
import com.fixhub.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
/**
 * 身份认证控制器，提供用户注册与登录接口。
 *
 * <p>本控制器仅包含演示用的最小接口，后续可扩展为 JWT / Session 等认证机制。</p>
 */
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册接口。
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 用户登录接口。
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}

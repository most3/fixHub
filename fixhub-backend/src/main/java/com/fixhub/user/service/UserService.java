package com.fixhub.user.service;

import com.fixhub.common.exception.DuplicateResourceException;
import com.fixhub.common.exception.UnauthorizedException;
import com.fixhub.user.dto.LoginRequest;
import com.fixhub.user.dto.LoginResponse;
import com.fixhub.user.dto.RegisterUserRequest;
import com.fixhub.user.dto.UserResponse;
import com.fixhub.user.model.User;
import com.fixhub.user.model.enums.UserRole;
import com.fixhub.user.mapper.UserMapper;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 负责注册与登录等基础账户能力。
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户服务构造函数
     *
    * @param userMapper 用户 Mapper
     * @param passwordEncoder 密码编码器，建议使用 BCrypt
     */
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 注册新用户并写入加密后的密码。
     */
    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        String normalizedUsername = request.getUsername().trim();
        if (userMapper.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new DuplicateResourceException("用户名已存在");
        }
        User user = new User();
        user.setUsername(normalizedUsername);
        user.setDisplayName(request.getDisplayName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.REPORTER);
        user.setCreatedAt(Instant.now());
        userMapper.insert(user);
        return UserResponse.fromModel(userMapper.selectById(user.getId()));
    }

    /**
     * 校验用户名与密码是否匹配，成功后返回登录结果。
     */
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectByUsernameIgnoreCase(request.getUsername());
        if (user == null) {
            throw new UnauthorizedException("用户名或密码不正确");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("用户名或密码不正确");
        }
        return LoginResponse.success(user.getId(), user.getUsername(), user.getRole());
    }
}

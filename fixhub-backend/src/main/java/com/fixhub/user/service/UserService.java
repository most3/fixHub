package com.fixhub.user.service;

import com.fixhub.common.exception.DuplicateResourceException;
import com.fixhub.common.exception.UnauthorizedException;
import com.fixhub.user.dto.LoginRequest;
import com.fixhub.user.dto.LoginResponse;
import com.fixhub.user.dto.RegisterUserRequest;
import com.fixhub.user.dto.UserResponse;
import com.fixhub.user.model.User;
import com.fixhub.user.model.enums.UserRole;
import com.fixhub.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户服务构造函数
     *
     * @param userRepository 用户仓库
     * @param passwordEncoder 密码编码器，建议使用 BCrypt
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        String normalizedUsername = request.getUsername().trim();
        if (userRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new DuplicateResourceException("Username already exists");
        }
        User user = new User();
        user.setUsername(normalizedUsername);
        user.setDisplayName(request.getDisplayName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.REPORTER);
        User saved = userRepository.save(user);
        return UserResponse.fromEntity(saved);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        return LoginResponse.success(user.getId(), user.getUsername(), user.getRole());
    }
}

package com.fixhub.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fixhub.common.exception.DuplicateResourceException;
import com.fixhub.common.exception.UnauthorizedException;
import com.fixhub.user.dto.LoginRequest;
import com.fixhub.user.dto.RegisterUserRequest;
import com.fixhub.user.dto.UserResponse;
import com.fixhub.user.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void registerCreatesUserAndHashesPassword() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setRole(UserRole.REPORTER);

        UserResponse response = userService.register(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo("alice");
        assertThat(response.getRole()).isEqualTo(UserRole.REPORTER);
    }

    @Test
    void registerWithDuplicateUsernameThrowsException() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("bob");
        request.setPassword("password123");
        userService.register(request);

        RegisterUserRequest duplicate = new RegisterUserRequest();
        duplicate.setUsername("bob");
        duplicate.setPassword("password456");

        assertThrows(DuplicateResourceException.class, () -> userService.register(duplicate));
    }

    @Test
    void loginWithInvalidPasswordThrowsException() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("charlie");
        request.setPassword("password123");
        userService.register(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("charlie");
        loginRequest.setPassword("wrong");

        assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
    }

    @Test
    void loginWithValidCredentialsReturnsUser() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("dana");
        request.setPassword("password123");
        request.setRole(UserRole.TECHNICIAN);
        userService.register(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("dana");
        loginRequest.setPassword("password123");

        assertThat(userService.login(loginRequest).getRole()).isEqualTo(UserRole.TECHNICIAN);
    }
}

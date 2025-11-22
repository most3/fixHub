package com.fixhub.user.dto;

import com.fixhub.user.model.enums.UserRole;

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
        response.setMessage("Login successful");
        return response;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

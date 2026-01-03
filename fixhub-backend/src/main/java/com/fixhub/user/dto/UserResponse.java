package com.fixhub.user.dto;

import com.fixhub.user.model.User;
import com.fixhub.user.model.enums.UserRole;
import java.time.Instant;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String displayName;
    private UserRole role;
    private Instant createdAt;

    public static UserResponse fromModel(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

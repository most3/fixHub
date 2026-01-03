package com.fixhub.user.model;

import com.fixhub.user.model.enums.UserRole;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;

    private String username;

    private String passwordHash;

    private String displayName;

    private UserRole role;

    private Instant createdAt = Instant.now();

}

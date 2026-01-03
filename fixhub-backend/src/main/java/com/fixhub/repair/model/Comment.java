package com.fixhub.repair.model;

import com.fixhub.user.model.User;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comment {

    private Long id;

    private RepairOrder order;

    private User user;

    private Integer rating;

    private String content;

    private Instant createdAt = Instant.now();

}

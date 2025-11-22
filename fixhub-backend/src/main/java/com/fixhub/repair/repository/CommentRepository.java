package com.fixhub.repair.repository;

import com.fixhub.repair.model.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByOrderId(Long orderId);
}

package com.fixhub.repair.mapper;

import com.fixhub.repair.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评价相关的 MyBatis Mapper。
 */
@Mapper
public interface CommentMapper {

    Comment selectById(@Param("id") Long id);

    Comment selectByOrderId(@Param("orderId") Long orderId);

    int insert(Comment comment);
}

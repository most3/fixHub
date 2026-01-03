package com.fixhub.user.mapper;

import com.fixhub.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户持久化操作 Mapper。
 */
@Mapper
public interface UserMapper {

    User selectById(@Param("id") Long id);

    User selectByUsernameIgnoreCase(@Param("username") String username);

    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    int insert(User user);

    int update(User user);
}

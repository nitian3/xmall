package com.yzsunlei.xmall.db.mapper;

import com.yzsunlei.xmall.db.model.User;

import java.util.List;

public interface UserMapper {
    List<User> selectByDto(User user);

    User selectById(Long id);

    User selectByUsername(String username);

    int deleteById(Long id);

    int insert(User user);

    int countByDto(User user);

    int updateById(User user);
}
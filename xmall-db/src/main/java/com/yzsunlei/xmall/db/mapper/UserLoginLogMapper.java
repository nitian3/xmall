package com.yzsunlei.xmall.db.mapper;

import com.yzsunlei.xmall.db.model.UserLoginLog;

import java.util.List;

public interface UserLoginLogMapper {
    int countByDto(UserLoginLog userLoginLog);

    int insert(UserLoginLog userLoginLog);

    List<UserLoginLog> selectByDto(UserLoginLog userLoginLog);

}
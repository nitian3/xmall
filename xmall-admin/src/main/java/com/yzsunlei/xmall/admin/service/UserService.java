package com.yzsunlei.xmall.admin.service;

import com.yzsunlei.xmall.admin.dto.CommonResult;
import com.yzsunlei.xmall.admin.dto.UserParam;
import com.yzsunlei.xmall.db.model.User;

import java.util.List;

/**
 * 后台管理员Service
 * Created by macro on 2018/4/26.
 */
public interface UserService {
    /**
     * 根据用户名获取后台管理员
     */
    User getAdminByUsername(String username);

    /**
     * 注册功能
     */
    CommonResult register(UserParam umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户
     */
    User getItem(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<User> list(String name, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    int update(User admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

}

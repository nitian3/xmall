package com.yzsunlei.xmall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.yzsunlei.xmall.admin.dto.CommonResult;
import com.yzsunlei.xmall.admin.dto.UserParam;
import com.yzsunlei.xmall.admin.service.UserService;
import com.yzsunlei.xmall.admin.util.JwtTokenUtil;
import com.yzsunlei.xmall.db.mapper.UserLoginLogMapper;
import com.yzsunlei.xmall.db.mapper.UserMapper;
import com.yzsunlei.xmall.db.model.User;
import com.yzsunlei.xmall.db.model.UserLoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * UserService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Override
    public User getAdminByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public CommonResult register(UserParam userParam) {
        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        //查询是否有相同用户名的用户
        User tmp = userMapper.selectByUsername(user.getUsername());
        if(tmp != null){
            return new CommonResult().failed("此用户已注册！");
        }

        //将密码进行加密操作
        String md5Password = passwordEncoder.encodePassword(user.getPassword(), null);
        user.setPassword(md5Password);
        userMapper.insert(user);
        return new CommonResult().success(user);
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encodePassword(password, null));
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        User user = getAdminByUsername(username);
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserId(user.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        userLoginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        User user = getAdminByUsername(username);
        user.setLoginTime(new Date());
        userMapper.updateById(user);
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public User getItem(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> list(String name, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        User user = new User();
        user.setUsername(name);
        return userMapper.selectByDto(user);
    }

    @Override
    public int update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public int delete(Long id) {
        return userMapper.deleteById(id);
    }
}

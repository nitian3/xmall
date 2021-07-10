package com.yzsunlei.xmall.db.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLoginLog implements Serializable {
    private Long id;

    private Long userId;

    private Date createTime;

    private String ip;

    private String address;

    /**
     * 浏览器登录类型
     *
     * @mbggenerated
     */
    private String userAgent;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", ip=").append(ip);
        sb.append(", address=").append(address);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
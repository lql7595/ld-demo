package com.liz.bean.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserInfo {
    
    private Long userId;
    private String username;
    private String email;
    private String role;
    private String token;
    private Long expireTime;
    
    public UserInfo() {}
    
    public UserInfo(Long userId, String username, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}

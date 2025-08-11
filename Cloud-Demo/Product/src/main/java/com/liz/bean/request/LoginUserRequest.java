package com.liz.bean.request;

import lombok.Data;

@Data
public class LoginUserRequest {

    private String userLogin;

    private String pwd;
    /**
     * 1.密码登录 2. github登录 3.ldap登录
     */
    private int loginType = 1;
}

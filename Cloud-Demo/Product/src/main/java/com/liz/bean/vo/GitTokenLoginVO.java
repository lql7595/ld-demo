package com.liz.bean.vo;

import lombok.Data;

@Data
public class GitTokenLoginVO {

    private String userLogin;

    private String token;
    /**
     * 1.密码登录 2. github登录 3.ldap登录
     */
    private int loginType = 2;
}

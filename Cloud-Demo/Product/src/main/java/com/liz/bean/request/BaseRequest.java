package com.liz.bean.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseRequest {
    @NotNull(message = "用户名不能为空")
    private String userLogin;
    @NotNull(message = "token不能为空")
    private String token;

    /**
     * 1.密码登录 2. github登录 3.ldap登录
     */
    private int loginType = 1;
}

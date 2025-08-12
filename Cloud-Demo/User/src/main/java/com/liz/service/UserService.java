package com.liz.service;

import com.liz.bean.request.BaseRequest;
import com.liz.bean.request.LoginUserRequest;
import com.liz.bean.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public interface UserService {
    BaseResponse login(LoginUserRequest request);

    BaseResponse queryUserInfo(BaseRequest request);

    BaseResponse ldapLogin(LoginUserRequest request);

    Set<String> getRoleRela(String role);

    BaseResponse githubCallback(String code);

    BaseResponse logout(BaseRequest request);

}

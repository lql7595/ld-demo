package com.liz.controller;

import com.liz.anno.TokenVerifyAnnotation;
import com.liz.bean.request.BaseRequest;
import com.liz.bean.request.LoginUserRequest;
import com.liz.bean.response.BaseResponse;
import com.liz.config.MyConfig;
import com.liz.constant.ErrorCode;
import com.liz.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/sys")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MyConfig myConfig;

    @PostMapping("/login")
    public BaseResponse loginUser(@RequestBody LoginUserRequest request) {
        return userService.login(request);
    }

    @PostMapping("/query/userinfo")
    @TokenVerifyAnnotation
    public BaseResponse queryUserInfo(@Validated @RequestBody BaseRequest request) {
        return userService.queryUserInfo(request);
    }

    @PostMapping("/login/ldap")
    public BaseResponse ldapLogin(@RequestBody LoginUserRequest request) {
        return userService.ldapLogin(request);
    }

    @GetMapping("/login/github")
    public BaseResponse redirectToGithub(HttpServletResponse response) throws IOException {
        String url = myConfig.getGithubAuthUrl() + "?client_id=" + myConfig.getGithubClientId();
        return BaseResponse.success(ErrorCode.SUCCESS, url);
    }

    @GetMapping("/oauth/callback")
    public BaseResponse githubCallback(@RequestParam("code") String code) {
        return userService.githubCallback(code);
    }

    @PostMapping("/logout")
    @TokenVerifyAnnotation
    public BaseResponse logout(@RequestBody BaseRequest request) {
        return userService.logout(request);
    }

    @GetMapping("/internal/role/rela")
    public Set<String> getRoleRela(@RequestParam("role") String role) {
        return userService.getRoleRela(role);
    }
}

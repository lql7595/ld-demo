package com.liz.service.impl;

import com.liz.bean.vo.GitTokenLoginVO;
import com.liz.exception.TokenVerifyException;
import com.liz.bean.entity.RoleRelationTEntity;
import com.liz.bean.entity.UserInfoEntity;
import com.liz.bean.request.BaseRequest;
import com.liz.bean.request.LoginUserRequest;
import com.liz.bean.response.BaseResponse;
import com.liz.bean.vo.UserRoleInfoVO;
import com.liz.config.MyConfig;
import com.liz.constant.Constant;
import com.liz.constant.ErrorCode;
import com.liz.mapper.UserInfoMapper;
import com.liz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MyConfig config;

    @Autowired
    private LdapTemplate ldapTemplate;
    Lock lock = new ReentrantLock(true);

    @Override
    public BaseResponse login(LoginUserRequest request) {
        UserInfoEntity dbUserInfo = userInfoMapper.queryUserInfo(request.getUserLogin());

        if (dbUserInfo == null) {
            return BaseResponse.failure(ErrorCode.USER_NO_USER_ERROR);
        }
        if (!request.getPwd().equals(dbUserInfo.getPwd())) {
            return BaseResponse.failure(ErrorCode.USER_USER_PWD_ERROR);
        }

        UserRoleInfoVO roleInfoVO = userInfoMapper.queryUserRoleInfo(dbUserInfo.getUserId());

        String token = setToken(dbUserInfo.getUserId(), roleInfoVO.getUserRoleTp());

        return BaseResponse.success(ErrorCode.SUCCESS, token);
    }

    @Override
    public BaseResponse queryUserInfo(BaseRequest request) {
        UserInfoEntity dbUserInfo = userInfoMapper.queryUserInfo(request.getUserLogin());
        return BaseResponse.success(ErrorCode.SUCCESS, dbUserInfo);
    }

    @Override
    public BaseResponse ldapLogin(LoginUserRequest request) {
        String baseDn = "ou=users";
        String filter = "(cn=" + request.getUserLogin() + ")";
        // 使用当前用户验证ldap认证
        boolean authenticate = ldapTemplate.authenticate(baseDn, filter, request.getPwd());
        String token = "";
        if (authenticate) {
            // 查询member 为当前用户的 角色组都有哪些。
            List<String> roles = ldapTemplate.search("ou=roles", "(member=cn=" + request.getUserLogin() + ",ou=users,dc=liz,dc=cn)", (AttributesMapper<String>) attr -> (String) attr.get("cn").get());
            String role = "";
            if (roles != null && !roles.isEmpty()) {
                role = roles.get(0);
            }
            token = setToken(request.getUserLogin(), role);
        }
        return authenticate ? BaseResponse.success(ErrorCode.SUCCESS, token) : BaseResponse.failure(ErrorCode.ERROR);
    }

    @Override
    public BaseResponse githubCallback(String code) {
        String url = config.getGithubAccTokenUrl();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", config.getGithubClientId());
        params.add("client_secret", config.getGithubClientSecret());
        params.add("code", code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);
        // 调用token接口获取token。
        // 这个token需要保存和用户关联，以便访问git内容，但现在不涉及访问git后续内容，因此先暂不处理。
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        String accessToken = "";
        try {
            accessToken = (String) response.getBody().get("access_token");
        } catch (NullPointerException e) {
            throw new TokenVerifyException(ErrorCode.ERROR);
        }

        // 调用user接口获取用户信息
        HttpHeaders getUserHttpHeaders = new HttpHeaders();
        getUserHttpHeaders.setBearerAuth(accessToken);
        HttpEntity<String> userRequest = new HttpEntity<>(getUserHttpHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(config.getGithubUserUrl(), HttpMethod.GET, userRequest, Map.class);
        Map userInfo = userResponse.getBody();

        // 获取用户惜和 生成一个本系统的token
        GitTokenLoginVO result = new GitTokenLoginVO();
        String userLogin = (String) userInfo.get("login");
        String token = setToken(userLogin, Constant.ROLE_INFO_ENUM.EDITOR.getRole());
        result.setUserLogin(userLogin);
        result.setToken(token);
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse logout(BaseRequest request) {
        redisTemplate.delete(Constant.REDIS_TOKEN_KEY_PREFIX + request.getUserLogin());
        return BaseResponse.success();
    }

    private Map<String, Set<String>> roleRelaMap;

    public Set<String> getRoleRela(String roleTp) {
        if (roleRelaMap != null) {
            return roleRelaMap.get(roleTp);
        }
        lock.lock();
        try {
            if (roleRelaMap == null) {
                List<RoleRelationTEntity> roleRelationTEntityList = userInfoMapper.queryAllRoleRela();
                if (roleRelationTEntityList != null) {
                    roleRelaMap = new HashMap<>();
                    for (RoleRelationTEntity relationT : roleRelationTEntityList) {
                        Set<String> values = roleRelaMap.computeIfAbsent(relationT.getRoleTp(), k -> new HashSet<>());
                        values.add(relationT.getRoleTpRela());
                    }
                }
            }
            return roleRelaMap.get(roleTp);
        } finally {
            lock.unlock();
        }
    }

    private String setToken(String userLogin, String roleTp) {
        // 登录成功，生成token
        // 采用随机生成并放到redis校验，不采用JWT方式校验。
        String token = UUID.randomUUID().toString().replace("-", "");
        // 将token放入redis
        redisTemplate.opsForValue().set(Constant.REDIS_TOKEN_KEY_PREFIX + userLogin, Constant.TOKEN_VALUE_PREFIX + token + "|" + roleTp, config.getTimeout(), TimeUnit.MILLISECONDS);
        return token;
    }
}

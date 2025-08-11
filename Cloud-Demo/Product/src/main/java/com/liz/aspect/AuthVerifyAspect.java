package com.liz.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.liz.exception.AuthVerifyException;
import com.liz.exception.TokenVerifyException;
import com.liz.anno.AuthVerifyAnnotation;
import com.liz.bean.request.BaseRequest;
import com.liz.bean.vo.UserRoleInfoVO;
import com.liz.constant.Constant;
import com.liz.mapper.UserInfoMapper;
import com.liz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.liz.constant.ErrorCode.USER_AUTH_ERROR;
import static com.liz.constant.ErrorCode.USER_NO_USER_ERROR;

@Aspect
@Component
@Slf4j
@Order(2)
public class AuthVerifyAspect {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserService userService;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Before("@annotation(authVerifyAnnotation)")
    public void verifyAuth(JoinPoint joinPoint, AuthVerifyAnnotation authVerifyAnnotation) {
        log.info("------------verify token ----------");
        log.info("request method name:{}", joinPoint.getSignature().getName());


        BaseRequest baseRequest = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseRequest) {
                baseRequest = (BaseRequest) arg;
            }
        }
        if (baseRequest == null) baseRequest = new BaseRequest();
        if (baseRequest.getLoginType() == 1) {
            // 密码登录
            handleVerify(baseRequest.getUserLogin(), authVerifyAnnotation.authList());
        } else if (baseRequest.getLoginType() == 2) {
            // github 登录
            handleVerify(baseRequest.getUserLogin(), authVerifyAnnotation.authList());
        } else if (baseRequest.getLoginType() == 3) {
            // ldap 登录
            handleVerify(baseRequest.getUserLogin(), authVerifyAnnotation.authList());
        } else {
            log.info("用户没有正确的login type 输入。type:{}", baseRequest.getLoginType());
            throw new AuthVerifyException(USER_AUTH_ERROR);
        }
    }

    private void handleVerify(String userLogin, String[] authList) {

        if (StrUtil.isBlank(userLogin)) {
            throw new AuthVerifyException(USER_NO_USER_ERROR);
        }
        if (authList == null || authList.length < 1) {
            // 如果没有指定接口要校验的身份，则认为该接口不校验。
            return;
        }
        String rdToken = stringRedisTemplate.opsForValue().get(Constant.REDIS_TOKEN_KEY_PREFIX + userLogin);
        String role = "";
        if (StrUtil.isNotBlank(rdToken)) {
            String[] split = rdToken.split("\\|");
            try {
                role = split[1];
            } catch (Exception ignored) {
                log.info("可能遇到了错误的token存储在redis 中。key：{}， values：{}", Constant.REDIS_TOKEN_KEY_PREFIX + userLogin, rdToken);
            }
        }
        if (role.isEmpty()){
            UserRoleInfoVO dbInfo = userInfoMapper.queryUserRoleInfo(userLogin);
            if (dbInfo == null || CollUtil.isEmpty(dbInfo.getRoleTpList())) {
                throw new AuthVerifyException(USER_AUTH_ERROR);
            }
            role = dbInfo.getUserRoleTp();

        }
        Set<String> roleRela = userService.getRoleRela(role);
        if (roleRela == null || roleRela.isEmpty()) {
            log.info("没有获取到正确的用户角色信息。 role:{}", role);
            throw new AuthVerifyException(USER_AUTH_ERROR);
        }
        // 判断用户是否包含这个接口的其中任意一个权限？
        boolean hasAuthFlag = false;
        for (String s : authList) {
            if (roleRela.contains(s)) {
                hasAuthFlag = true;
                break;
            }
        }
        if (!hasAuthFlag) {
            throw new AuthVerifyException(USER_AUTH_ERROR);

        }
    }

//    private void handleVerifyLdap(String userLogin, String[] authList) {
//        String usrDn = "cn=" + userLogin + ",ou=users,dc=liz,dc=cn";
//
//        List<String> roles = ldapTemplate.search("ou=roles,dc=liz,dc=cn", "member=" + usrDn, (AttributesMapper<String>) attr -> (String) attr.get("cn").get());
//        String role = "";
//        if (roles != null && !roles.isEmpty()) {
//            role = roles.get(0);
//        }
//
//        Constant.ROLE_INFO_ENUM roleInfoEnum = Constant.ROLE_INFO_ENUM.VALUES_MAP.get(role);
//        if (roleInfoEnum == null) {
//            throw new AuthVerifyException(USER_AUTH_ERROR);
//        }
//        boolean resultFlag = false;
//        for (String auth : authList) {
//            resultFlag = roleInfoEnum.getAuthList().contains(auth);
//            if (resultFlag) {
//                break;
//            }
//        }
//        if (!resultFlag) {
//            throw new TokenVerifyException(USER_AUTH_ERROR);
//
//        }
//    }
}

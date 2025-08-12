package com.liz.aspect;

import cn.hutool.core.util.StrUtil;
import com.liz.exception.TokenVerifyException;
import com.liz.anno.TokenVerifyAnnotation;
import com.liz.bean.request.BaseRequest;
import com.liz.constant.Constant;
import com.liz.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(1)
public class TokenVerifyAspect {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Before("@annotation(tokenVerifyAnnotation)")
    public void verifyToken(JoinPoint joinPoint, TokenVerifyAnnotation tokenVerifyAnnotation) {
        log.info("------------verify token ----------");
        log.info("request method name:{}", joinPoint.getSignature().getName());

        BaseRequest baseRequest = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseRequest) {
                baseRequest = (BaseRequest) arg;
            }
        }
        if (baseRequest == null) baseRequest = new BaseRequest();
        handleVerify(baseRequest.getUserLogin(), baseRequest.getToken());
    }

    private void handleVerify(String userLogin, String token) {

        if (StrUtil.isBlank(userLogin)) {
            throw new TokenVerifyException(ErrorCode.USER_NO_USER_ERROR);
        }
        if (StrUtil.isBlank(token)) {
            throw new TokenVerifyException(ErrorCode.USER_NO_TOKEN_ERROR);
        }
        // redis 中的token 带有前缀
        String rdToken = redisTemplate.opsForValue().get(Constant.REDIS_TOKEN_KEY_PREFIX + userLogin);
        if (StrUtil.isBlank(rdToken)) {
            log.info("user:{} token:{} redisToken:{} .... verify failure.", userLogin, token, rdToken);
            throw new TokenVerifyException(ErrorCode.USER_TOKEN_VERIFY_FAILURE_ERROR);
        }
        String[] split = rdToken.split("\\|");
        // 将参数传入的不带前缀的token 加上前缀（简单的防token碰撞）
        String finalToken = Constant.TOKEN_VALUE_PREFIX + token;

        if (!finalToken.equals(split[0])) {
            log.info("user:{} token:{} redisToken:{} .... verify failure.", userLogin, token, rdToken);
            throw new TokenVerifyException(ErrorCode.USER_TOKEN_VERIFY_FAILURE_ERROR);
        }

    }
}

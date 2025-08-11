package com.liz.interceptor;

import com.liz.exception.AuthVerifyException;
import com.liz.exception.TokenVerifyException;
import com.liz.bean.response.BaseResponse;
import com.liz.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse<String> handleException(Exception e) {
        String eMsg = e.getMessage();
        log.error("异常信息: {}", eMsg, e);
        return BaseResponse.failure(ErrorCode.ERROR);
    }
    
    @ExceptionHandler(TokenVerifyException.class)
    public BaseResponse<String> handleTokenVerifyException(TokenVerifyException e) {
        String eMsg = e.getMessage();
        log.error("Token验证异常: {}", eMsg, e);
        return BaseResponse.failure(e.getErrorCode());
    }
    @ExceptionHandler(AuthVerifyException.class)
    public BaseResponse<String> handleTokenVerifyException(AuthVerifyException e) {
        String eMsg = e.getMessage();
        log.error("鉴权异常: {}", eMsg, e);
        return BaseResponse.failure(e.getErrorCode());
    }
}

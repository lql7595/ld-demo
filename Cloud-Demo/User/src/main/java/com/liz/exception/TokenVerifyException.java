package com.liz.exception;

import com.liz.constant.ErrorCode;
import lombok.Getter;

@Getter
public class TokenVerifyException extends RuntimeException {

    private final ErrorCode errorCode;

    TokenVerifyException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode  = errorCode;
    }

    public TokenVerifyException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode  = errorCode;
    }

}

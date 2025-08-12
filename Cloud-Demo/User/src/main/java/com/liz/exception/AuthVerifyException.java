package com.liz.exception;

import com.liz.constant.ErrorCode;
import lombok.Getter;

@Getter
public class AuthVerifyException extends RuntimeException {

    private final ErrorCode errorCode;

    AuthVerifyException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode  = errorCode;
    }

    public AuthVerifyException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode  = errorCode;
    }

}

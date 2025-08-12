package com.liz.bean.response;

import com.liz.constant.ErrorCode;

import cn.hutool.json.JSONUtil;
import lombok.Data;

@Data
public class BaseResponse<T> {

    private int errorCode;
    private String message;

    private T data;

    BaseResponse(int errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }
    
    BaseResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getErrorCode(), ErrorCode.SUCCESS.getMsg(), data);
    }
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ErrorCode.SUCCESS.getErrorCode(), ErrorCode.SUCCESS.getMsg());
    }
    public static <T> BaseResponse<T> success(ErrorCode errorCode, T data) {
        return new BaseResponse<>(errorCode.getErrorCode(), errorCode.getMsg(), data);
    }

    public static <T> BaseResponse<T> success(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getErrorCode(), errorCode.getMsg());
    }

    public static <T> BaseResponse<T> failure(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getErrorCode(), errorCode.getMsg());
    }

    
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}

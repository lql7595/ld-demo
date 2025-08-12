package com.liz.bean.response;

import lombok.Data;

/**
 * 基础响应类
 */
@Data
public class BaseResponse<T> {
    
    private String code;
    private String message;
    private T data;
    private long timestamp;
    
    public BaseResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public BaseResponse(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public BaseResponse(String code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>("200", "操作成功");
    }
    
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>("200", "操作成功", data);
    }
    
    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(code, message);
    }
    
    public static <T> BaseResponse<T> unauthorized(String message) {
        return new BaseResponse<>("401", message);
    }
    
    public static <T> BaseResponse<T> forbidden(String message) {
        return new BaseResponse<>("403", message);
    }
    
    public static <T> BaseResponse<T> serverError(String message) {
        return new BaseResponse<>("500", message);
    }
}

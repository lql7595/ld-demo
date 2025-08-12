package com.liz.constant;

/**
 * 常量类
 */
public class Constant {
    
    /**
     * 认证相关常量
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_NAME_HEADER = "X-User-Name";
    
    /**
     * Redis相关常量
     */
    public static final String TOKEN_KEY_PREFIX = "token:";
    public static final String USER_KEY_PREFIX = "user:";
    public static final long TOKEN_EXPIRE_TIME = 7200L; // 2小时
    
    /**
     * 响应相关常量
     */
    public static final String SUCCESS_CODE = "200";
    public static final String UNAUTHORIZED_CODE = "401";
    public static final String FORBIDDEN_CODE = "403";
    public static final String ERROR_CODE = "500";
    
    /**
     * 路径相关常量
     */
    public static final String LOGIN_PATH = "/api/user/login";
    public static final String REGISTER_PATH = "/api/user/register";
    public static final String HEALTH_PATH = "/health";
    
    /**
     * 错误消息
     */
    public static final String TOKEN_MISSING = "Token缺失";
    public static final String TOKEN_INVALID = "Token无效";
    public static final String TOKEN_EXPIRED = "Token已过期";
    public static final String UNAUTHORIZED_ACCESS = "未授权访问";
    public static final String SERVICE_UNAVAILABLE = "服务不可用";
}

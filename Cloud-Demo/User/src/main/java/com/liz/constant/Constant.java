package com.liz.constant;

import lombok.Getter;

import java.util.*;

public class Constant {

    // redis存放token的 key 前缀
    public static final String REDIS_TOKEN_KEY_PREFIX = "LD_USR_TP1_";

    // 生成的token的 内部用前缀，防外部撞击token的UUID
    public static final String TOKEN_VALUE_PREFIX = "LD_USR_TP1_";
    public static final String USER = "USER";
    public static final String EDITOR = "EDITOR";
    public static final String ADMIN = "ADMIN";

    @Getter
    public enum ROLE_INFO_ENUM {
        USER(Constant.USER, Collections.singletonList(Constant.USER)),
        EDITOR(Constant.EDITOR,Arrays.asList(Constant.USER, Constant.EDITOR)),
        ADMIN(Constant.ADMIN, Arrays.asList(Constant.USER, Constant.EDITOR, Constant.ADMIN));
        final String role;
        final List<String> authList;

        ROLE_INFO_ENUM(String role, List<String> authList) {
            this.role = role;
            this.authList = authList;
        }

        public static final Map<String, ROLE_INFO_ENUM> VALUES_MAP = new HashMap<>();

        static {
            VALUES_MAP.put(USER.role, USER);
            VALUES_MAP.put(EDITOR.role, EDITOR);
            VALUES_MAP.put(ADMIN.role, ADMIN);
        }
    }

}

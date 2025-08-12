package com.liz.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ERROR(500, "系统异常"),
    SUCCESS(200, "成功"),
    SYSTEM_ERROR(500, "系统错误"),
    USER_NO_USER_ERROR(601, "用户不存在"),
    USER_USER_PWD_ERROR(602, "密码错误"),
    USER_NO_TOKEN_ERROR(603, "token错误"),
    USER_TOKEN_VERIFY_FAILURE_ERROR(604, "token验证失败"),
    USER_AUTH_ERROR(605, "没有权限访问此接口" ),
    PRODUCT_DEL_ERROR(606, "删除商品失败，请刷新界面重试。" ),
    PRODUCT_UPD_ERROR(607, "更新商品失败，请刷新界面重试。" ),
    PRODUCT_SAVE_ERROR(608, "新增商品失败，请刷新界面重试。" );

    final int errorCode;
    final String msg;

    ErrorCode(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }
}

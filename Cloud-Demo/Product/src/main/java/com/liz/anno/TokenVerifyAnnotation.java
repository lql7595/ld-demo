package com.liz.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenVerifyAnnotation {
    String userLogin() default "userLogin";
    String token() default "token";
}

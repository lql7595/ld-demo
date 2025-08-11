package com.liz.anno;

import com.liz.constant.Constant;

import java.lang.annotation.*;
import java.util.concurrent.ConcurrentHashMap;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthVerifyAnnotation {
    String[] authList() default "";

}

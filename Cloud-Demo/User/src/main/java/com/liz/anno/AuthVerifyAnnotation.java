package com.liz.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthVerifyAnnotation {
    String[] authList() default "";
}

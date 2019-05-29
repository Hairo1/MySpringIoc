package com.hairo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
@Target({ElementType.TYPE})//只能在类上加入
@Retention(value = RetentionPolicy.RUNTIME)//运行时保留
public @interface HairoService {
    String value() default "";

}

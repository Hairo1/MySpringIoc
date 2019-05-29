package com.hairo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})//只能在类上加入
@Retention(value = RetentionPolicy.RUNTIME)//运行时保留
public @interface HairoController {
    String value() default "";
}

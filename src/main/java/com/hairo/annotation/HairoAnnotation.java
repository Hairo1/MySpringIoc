package com.hairo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})//只能在属性
@Retention(value = RetentionPolicy.RUNTIME)//运行时保留
public @interface HairoAnnotation {
    String value() default "";
}

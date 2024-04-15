package com.seciii.prism030.core.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * @auther: LiDongSheng
 * @date: 2024.4.15
 * 添加新闻后更新redis的新闻数量
 */
public @interface Add {
    String value() default "";
}

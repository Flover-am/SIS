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
 * 更新新闻后更新redis的最后修改时间
 */
public @interface Modified {
    String value() default "";
}

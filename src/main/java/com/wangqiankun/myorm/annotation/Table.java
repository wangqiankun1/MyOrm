package com.wangqiankun.myorm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述:
 * 标记实体类在数据库中对应的表
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/11 14:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}

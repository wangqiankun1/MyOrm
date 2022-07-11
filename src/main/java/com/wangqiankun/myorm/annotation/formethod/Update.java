package com.wangqiankun.myorm.annotation.formethod;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 功能描述:
 * 用于用户自定义更新方法
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/11 14:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Update {
    @AliasFor("sql")
    String value() default "";

    @AliasFor("value")
    String sql() default "";
}

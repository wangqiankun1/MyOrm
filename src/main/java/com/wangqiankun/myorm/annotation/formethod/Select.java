package com.wangqiankun.myorm.annotation.formethod;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 功能描述:
 * 用于自定义查询方法。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/11 14:07
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    @AliasFor("sql")
    String value() default "";

    @AliasFor("value")
    String sql() default "";
}

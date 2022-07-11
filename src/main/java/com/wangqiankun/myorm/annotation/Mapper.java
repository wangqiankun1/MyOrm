package com.wangqiankun.myorm.annotation;

import java.lang.annotation.*;


/**
 * 功能描述:
 * 标志需要动态代理的接口，扫描到被该注解标识的接口就会实现自动代理。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/11 14:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Mapper {
    String value() default "";
}

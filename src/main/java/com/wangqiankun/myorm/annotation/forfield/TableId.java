package com.wangqiankun.myorm.annotation.forfield;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 功能描述:
 * 用于标记实体类中在数据库表中的物理主键
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/11 14:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}

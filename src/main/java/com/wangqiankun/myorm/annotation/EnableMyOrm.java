package com.wangqiankun.myorm.annotation;

import com.wangqiankun.myorm.config.DataSourceInjectBeanPostProcessor;
import com.wangqiankun.myorm.MyOrmAutoConfiguration;
import com.wangqiankun.myorm.registrar.MapperProxyRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述:
 * 自动开起MyOrm框架注解。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/5 10:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({MapperProxyRegistrar.class, MyOrmAutoConfiguration.class, DataSourceInjectBeanPostProcessor.class})
public @interface EnableMyOrm {
    @AliasFor("basePackage") String value() default "";

    @AliasFor("value") String basePackage() default "";
}

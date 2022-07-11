package com.wangqiankun.myorm.registrar;

import com.wangqiankun.myorm.annotation.EnableMyOrm;
import com.wangqiankun.myorm.annotation.Mapper;
import com.wangqiankun.myorm.mapper.BaseMapper;
import com.wangqiankun.myorm.proxy.MapperProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * 功能描述:
 * 用于注入动态代理生成的对象，注入的实际是MapperProxyFactory，MapperProxyFactory实现
 * FactoryBean接口并重写getObject方法，该方法返回要注入容器的代理对象。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/5 16:05
 */
@Configuration
@Slf4j
public class MapperProxyRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyOrm.class.getName()));
        assert annoAttrs != null;
        String basePackage = ((String) annoAttrs.get("basePackage"));
        Objects.requireNonNull(basePackage);

        //从basePackage包中扫描带有Mapper注解且继承BaseMapper的接口。
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> mapperClasses = reflections.get(SubTypes.of(TypesAnnotated.with(Mapper.class)).asClass()).stream().filter(clazz -> clazz.isInterface() && BaseMapper.class.isAssignableFrom(clazz)).collect(Collectors.toSet());

        mapperClasses.forEach(clazz -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
            beanDefinition.setBeanClass(MapperProxyFactory.class);
            beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            String beanName = importBeanNameGenerator.generateBeanName(beanDefinition, registry);
            registry.registerBeanDefinition(beanName, beanDefinition);
        });

    }
}

package com.wangqiankun.myorm.config;

import com.wangqiankun.myorm.proxy.MapperProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 功能描述:
 * 用于给代理工厂MapperProxyFactory注入DataSource，此处实现Spring提供的BeanPostProcessor接口，编写回调逻辑。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/6 11:42
 */
@Configuration
@Slf4j
public class DataSourceInjectBeanPostProcessor implements BeanPostProcessor {
    private final DataSource dataSource;

    public DataSourceInjectBeanPostProcessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.equals(MapperProxyFactory.class, bean.getClass())) {
            MapperProxyFactory<?> factory = (MapperProxyFactory<?>) bean;
            factory.setDataSource(this.dataSource);
        }
        return bean;
    }

}

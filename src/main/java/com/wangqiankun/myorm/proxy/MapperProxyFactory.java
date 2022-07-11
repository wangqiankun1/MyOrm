package com.wangqiankun.myorm.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

/**
 * 功能描述:
 * 代理工厂，用于生成代理对象。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/4 14:49
 */
@Slf4j
public class MapperProxyFactory<T> implements FactoryBean<Object> {
    private final Class<T> mapperInterface;
    private DataSource dataSource;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance() {
        MapperProxy mapperProxy = new MapperProxy(this.dataSource);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    @Override
    public Object getObject() {
        log.info(this.getClass().getSimpleName() + "::getObject 方法被执行");
        return this.newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return this.newInstance().getClass();
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

package com.wangqiankun.myorm.proxy;

import com.wangqiankun.myorm.annotation.formethod.Select;
import com.wangqiankun.myorm.annotation.formethod.Update;
import com.wangqiankun.myorm.mapper.BaseMapper;
import com.wangqiankun.myorm.mapper.DefaultBaseMapper;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.*;
import java.util.Objects;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/4 13:54
 */
@Slf4j
public class MapperProxy implements InvocationHandler {
//    private final Class<T> mapperInterface;

    @Resource
    private DataSource dataSource;

    public MapperProxy(DataSource dataSource) {
//        this.mapperInterface = mapperInterface;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            log.info("Object方法被执行，无需代理");
            return method.invoke(this, args);
        }
        /*
          判断是否为用户自定义的方法，若是进入if块
         */
        Class<?> declaringClass = method.getDeclaringClass();
        if (!Objects.equals(declaringClass, BaseMapper.class)) {
            if (!(method.isAnnotationPresent(Select.class) || method.isAnnotationPresent(Update.class))) {
                throw new RuntimeException("无法代理的方法");
            }
            Select selectAnno = method.getAnnotation(Select.class);
            Update updateAnno = method.getAnnotation(Update.class);
            if (Objects.nonNull(selectAnno) && Objects.nonNull(updateAnno)) {
                throw new RuntimeException("注解冲突");
            }
            // TODO: 2022/7/7 自定义方法执行，待完善
        }

        /*
          以下三行代码用于获取BaseMapper子接口上的泛型具体类型。
         */
        Type[] genericInterfaces = proxy.getClass().getGenericInterfaces();
        Type genericInterface = ((Class<?>) genericInterfaces[0]).getGenericInterfaces()[0];
        String typeName = ((ParameterizedTypeImpl) genericInterface).getActualTypeArguments()[0].getTypeName();

        DefaultBaseMapper defaultBaseMapper = new DefaultBaseMapper(this.dataSource, Class.forName(typeName));
        Method[] defaultBaseMapperMethods = DefaultBaseMapper.class.getMethods();
        for (Method m : defaultBaseMapperMethods) {
            if (method.getName().equals(m.getName())) {
                return m.invoke(defaultBaseMapper, args);
            }
        }
        return method.invoke(proxy, args);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}


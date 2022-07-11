package com.wangqiankun.myorm.executor;

import javax.sql.DataSource;
import java.util.List;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/6/30 17:13
 */
public interface DmlExecutor extends Executor {
    /**
     * 插入一条记录
     *
     * @param data      需要插入的记录
     * @param tableName 插入的表名
     * @return 插入成功返回true，插入失败返回false
     */
    boolean insert(Object data, String tableName);

    boolean insertBath(List data, String tableName);

    /**
     * 查询功能
     *
     * @param dataSource 数据源
     * @param args 代理方法传入的参数
     * @param sql 方法的SQL语句
     * @param returnType 代理方法的返回值类型
     * @return
     */
    <T> T select(DataSource dataSource, Object[] args, String sql, T returnType);
}

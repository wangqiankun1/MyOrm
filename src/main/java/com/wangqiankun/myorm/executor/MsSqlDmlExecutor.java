package com.wangqiankun.myorm.executor;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/1 9:49
 */
@Slf4j
public class MsSqlDmlExecutor implements DmlExecutor {

    @Resource
    private DataSource dataSource;

    @Override
    public boolean insert(Object data, String tableName) {
        return false;
    }

    @Override
    public boolean insertBath(List data, String tableName) {
        return false;
    }

    @Override
    public <T> T select(DataSource dataSource, Object[] args, String sql, T returnType) {
        return null;
    }

}

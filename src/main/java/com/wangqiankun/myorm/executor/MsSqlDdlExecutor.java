package com.wangqiankun.myorm.executor;


import com.wangqiankun.myorm.entity.TableInfo;
import com.wangqiankun.myorm.entity.View;
import com.wangqiankun.myorm.utils.SqlBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/1 9:46
 */
@Slf4j
public class MsSqlDdlExecutor implements DdlExecutor {

    @Resource
    private DataSource msSqlDataSource;

    @Override
    public boolean createTable(TableInfo t) {
        Connection connection;
        PreparedStatement dropTableStatement;
        PreparedStatement createTableStatement;
        try {
            connection = msSqlDataSource.getConnection();
            dropTableStatement = connection.prepareStatement("drop table if exists  " + t.getTableName());
            dropTableStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = SqlBuilder.buildCreateTableSql(t);
        try {
            log.info("执行SQL：\n" + sql);
            createTableStatement = connection.prepareStatement(sql);
            createTableStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createView(View view)  {
        return false;
    }
}

package com.wangqiankun.myorm.executor;

import com.wangqiankun.myorm.utils.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/1 9:37
 */
@Slf4j
public class MySqlDmlExecutor implements DmlExecutor {

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
        Connection connection = null;
        String newSql = SqlBuilder.concatSql(sql, args);
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(newSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}

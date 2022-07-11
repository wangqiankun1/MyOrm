package com.wangqiankun.myorm.mapper;

import com.wangqiankun.myorm.annotation.Table;
import com.wangqiankun.myorm.utils.SqlBuilder;
import com.wangqiankun.myorm.wrapper.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述:
 * BaseMapper的默认实现，在代理逻辑中待用该类中的方法。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/7 13:52
 */
@Slf4j
public class DefaultBaseMapper implements BaseMapper<Object> {
    private final DataSource dataSource;
    private final Class<?> entityType;
    private final String tableName;
    private final Field[] fields;

    public DefaultBaseMapper(DataSource dataSource, Class<?> entityType) {
        this.dataSource = dataSource;
        this.entityType = entityType;
        tableName = entityType.getAnnotation(Table.class).value();
        fields = entityType.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
    }

    @Override
    public Object selectOne(QueryWrapper queryWrapper) throws RuntimeException {
        Connection connection;
        Statement statement;
        ResultSet resultSet;

        String sql = SqlBuilder.createSelectOneSql(tableName, this.fields, queryWrapper);
        log.info("执行SQL:{}", sql);
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            resultSet.last();
            int count = resultSet.getRow();
            if (count > 1) {
                throw new RuntimeException("结果大于预期");
            }
            if (count == 0) {
                return null;
            }
            resultSet.beforeFirst();
            resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Object returnObj = entityType.newInstance();
            Arrays.stream(fields).forEach(field -> {
                try {
                    field.set(returnObj, resultSet.getObject(SqlBuilder.getFiledName(field)));
                } catch (IllegalAccessException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            return returnObj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Object> selectList(QueryWrapper queryWrapper) {
        Connection connection;
        Statement statement;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = SqlBuilder.createSelectListSql(tableName, fields, queryWrapper);
        log.info("执行SQL：{}", sql);
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            List<Object> result = new LinkedList<>();
            while (resultSet.next()) {
                Object obj = entityType.newInstance();
                Arrays.stream(fields).forEach(field -> {
                    try {
                        field.set(obj, resultSet.getObject(SqlBuilder.getFiledName(field)));
                    } catch (IllegalAccessException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                result.add(obj);
            }
            return result;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insert(Object param) {
        Objects.requireNonNull(param);
        if (!Objects.equals(param.getClass(), this.entityType)) {
            throw new IllegalArgumentException("标注类型与传入类型不一致");
        }
        Connection connection;
        Statement statement;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = SqlBuilder.buildInsertSql(tableName, fields, param);
        log.info("执行SQL：{}" + sql);
        try {
            int updateCount = statement.executeUpdate(sql);
            return updateCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insertBath(List<Object> objects) {
        Objects.requireNonNull(objects);
        Connection connection;
        String preparedSql = SqlBuilder.createInsertBathSql(tableName, fields);
        final PreparedStatement preparedStatement;
        try {
            connection = dataSource.getConnection();
            log.info("执行SQL:{}", preparedSql);
            preparedStatement = connection.prepareStatement(preparedSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            objects.forEach(obj -> {
                AtomicInteger parameterIndex = new AtomicInteger(1);
                Arrays.stream(fields).forEachOrdered(field -> {
                    try {
                        preparedStatement.setObject(parameterIndex.getAndIncrement(), field.get(obj));
                    } catch (SQLException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                try {
                    preparedStatement.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            int[] ints = preparedStatement.executeBatch();
            return (int) Arrays.stream(ints).filter(result -> result > 0).count();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
                if (Objects.nonNull(preparedStatement)) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Connection connection;
        Statement statement;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = SqlBuilder.createDeleteByIdSql(tableName, this.fields, id);
        log.info("执行SQl：{}", sql);
        try {
            int updateCount = statement.executeUpdate(sql);
            return updateCount > 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

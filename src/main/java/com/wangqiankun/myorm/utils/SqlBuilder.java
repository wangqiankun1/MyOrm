package com.wangqiankun.myorm.utils;

import com.wangqiankun.myorm.annotation.forfield.TableField;
import com.wangqiankun.myorm.annotation.forfield.TableId;
import com.wangqiankun.myorm.entity.Field;
import com.wangqiankun.myorm.entity.TableInfo;
import com.wangqiankun.myorm.wrapper.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 功能描述:
 * 用于拼接DefaultMapper中模板方法需要的sql
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/4 9:35
 */
@Slf4j
public class SqlBuilder {
    public static String buildCreateTableSql(TableInfo t) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE " + t.getTableName() + " (\n");
        List<Field> fields = t.getFields();
        for (Field f : fields) {
            sqlBuilder.append("\t").append(f.getName()).append(" ").append(f.getType()).append(SqlBuilder.fieldLengthToString(f.getLength())).append(",\n");
        }
        int index = sqlBuilder.lastIndexOf(",");
        return sqlBuilder.delete(index, index + 1).append(")").toString();
    }


    /**
     * 用于自定义sql的参数拼接，参数按顺序替换‘${parameter_name}’
     *
     * @param sql  用户自己编写的sql
     * @param args sql参数
     * @return 拼接完的sql
     */
    public static String concatSql(String sql, Object[] args) {
        String regex = "\\$\\{\\w+}";
//        String regex = "\\$\\{\\w+\\}";
        for (Object arg : args) {
            sql = sql.replaceFirst(regex, String.valueOf(arg));
        }
        return sql;
    }

    public static String createSelectOneSql(String tableName, java.lang.reflect.Field[] fields, QueryWrapper queryWrapper) {
        Objects.requireNonNull(queryWrapper);
        if (queryWrapper.getEqualsMap().isEmpty()) {
            throw new IllegalArgumentException("需要有参数");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select");

        StringJoiner columnJoiner = new StringJoiner(",", " ", " ");
        for (java.lang.reflect.Field field : fields) {
            columnJoiner.add(getFiledName(field));
        }
        stringBuilder.append(columnJoiner);

        stringBuilder.append("from ").append(tableName).append(" where ");
        StringJoiner queryConditionsJoiner = new StringJoiner(" and ");
        Map<String, Object> equalsMap = queryWrapper.getEqualsMap();
        equalsMap.forEach((column, value) -> queryConditionsJoiner.add(column + " = " + "'" + value + "'"));
        stringBuilder.append(queryConditionsJoiner);
        return stringBuilder.toString();
    }


    public static String getFiledName(java.lang.reflect.Field field) {
        Objects.requireNonNull(field);
        String fieldName;
        TableField tableFieldAnno = field.getAnnotation(TableField.class);
        if (Objects.nonNull(tableFieldAnno)) {
            fieldName = tableFieldAnno.value();
        } else {
            fieldName = field.getName().toLowerCase();
        }
        return fieldName;
    }

    public static String buildInsertSql(String tableName, java.lang.reflect.Field[] fields, Object param) {
        StringBuilder stringBuilder = new StringBuilder("insert into ");
        StringBuilder valueBuilder = new StringBuilder("values (");
        stringBuilder.append(tableName).append(" (");
        for (java.lang.reflect.Field field : fields) {
            stringBuilder.append(getFiledName(field)).append(",");
            try {
                valueBuilder.append("'").append(field.get(param)).append("'").append(",");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(",")).append(") ");
        valueBuilder.deleteCharAt(valueBuilder.lastIndexOf(",")).append(")");
        return stringBuilder.append(valueBuilder).toString();
    }

    public static String createDeleteByIdSql(String tableName, java.lang.reflect.Field[] fields, Long id) {
        StringBuilder stringBuilder = new StringBuilder();
        Optional<java.lang.reflect.Field> idFieldOptional = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(TableId.class)).findFirst();

        if (!idFieldOptional.isPresent()) {
            throw new RuntimeException("需要指定Id列");
        }
        return stringBuilder.append("delete from ").append(tableName).append(" where ").append(idFieldOptional.get().getName()).append(" = ").append("'").append(id).append("'").toString();
    }

    public static String createSelectListSql(String tableName, java.lang.reflect.Field[] fields, QueryWrapper queryWrapper) {
        StringBuilder stringBuilder = new StringBuilder("select ");
        Arrays.stream(fields).forEach(field -> stringBuilder.append(getFiledName(field)).append(", "));
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("from ").append(tableName).append(" ");
        if (Objects.isNull(queryWrapper)) {
            return stringBuilder.toString();
        }
        stringBuilder.append("where ");
        queryWrapper.getEqualsMap().forEach((column, value) -> stringBuilder.append(column).append(" = ").append("'").append(value).append("'").append(" and "));
        int index = stringBuilder.indexOf("and");
        stringBuilder.delete(index, index + 3);
        return stringBuilder.toString();
    }

    public static String createInsertBathSql(String tableName, java.lang.reflect.Field[] fields) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into ").append(tableName);

        StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
        Arrays.stream(fields).forEachOrdered(field -> columnJoiner.add(getFiledName(field)));
        stringBuilder.append(columnJoiner);

        StringJoiner valueJoiner = new StringJoiner(",", "(", ")");
        stringBuilder.append(" values ");
        for (int i = 0; i < fields.length; i++) {
            valueJoiner.add("?");
        }
        return stringBuilder.append(valueJoiner).toString();
    }

    private static String fieldLengthToString(Integer length) {
        if (length == null) {
            return "";
        }
        return "(" + length + ")";
    }
}

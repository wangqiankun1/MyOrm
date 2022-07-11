package com.wangqiankun.myorm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 功能描述:
 * 封装数据库表结构
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/6/29 17:00
 */
@Data
@AllArgsConstructor
public class TableInfo {
    private String tableName;
    private List<Field> fields;
    private boolean dropIfPresent;
    private String charSet;
}

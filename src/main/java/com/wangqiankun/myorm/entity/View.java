package com.wangqiankun.myorm.entity;

import lombok.Data;

import java.util.Map;

/**
 * 功能描述:
 * 封装视图结构
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/1 11:04
 */
@Data
public class View {
    private String name;
    /**
     * key:字段名,value:别名
     */
    private Map<String, String> selects;
    private String fromTableName;
    private Map<String, String> conditions;
}

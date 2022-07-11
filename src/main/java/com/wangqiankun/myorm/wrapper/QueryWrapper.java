package com.wangqiankun.myorm.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:
 * 用户封装查询条件，为了演示，只封装equals（‘=’）条件
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/7 10:12
 */
public class QueryWrapper {

    /**
     * 封装使用‘=’的查询条件，其他类似
     */
    private final Map<String, Object> equalsMap = new HashMap<>();

    public QueryWrapper eq(String key, Object value) {
        equalsMap.put(key, value);
        return this;
    }


    public Map<String, Object> getEqualsMap() {
        return equalsMap;
    }

}

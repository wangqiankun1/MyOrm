package com.wangqiankun.myorm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能描述:
 * 数据库表中对应的字段。
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/6/29 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private String name;
    private String type;
    private Integer length;
}

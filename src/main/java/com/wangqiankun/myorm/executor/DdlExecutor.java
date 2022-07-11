package com.wangqiankun.myorm.executor;

import com.wangqiankun.myorm.entity.TableInfo;
import com.wangqiankun.myorm.entity.View;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/1 9:42
 */
public interface DdlExecutor extends Executor {
    /**
     * 根据表结构信息创建表
     *
     * @param t 表结构信息
     * @return 创建成功返回true，创建失败返回false
     */
    boolean createTable(TableInfo t);

    /**
     * 根据视图结果创建视图
     * @param view 视图结构
     * @return 创建成功返回true，创建失败返回false
     */
    boolean createView(View view);
}

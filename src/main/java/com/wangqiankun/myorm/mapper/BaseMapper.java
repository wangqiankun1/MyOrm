package com.wangqiankun.myorm.mapper;

import com.wangqiankun.myorm.wrapper.QueryWrapper;

import java.util.List;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/7/7 10:09
 */
public interface BaseMapper<T> {

    /**
     * 根据查询条件查询一条记录
     * @param queryWrapper 封装查询条件
     * @return 封装查询结果
     * @throws RuntimeException 如果查询的记录多于一条，则抛出该异常
     */
    T selectOne(QueryWrapper queryWrapper) throws RuntimeException;

    /**
     * 根据查询条件查询一组满足的记录
     * @param queryWrapper 查询条件
     * @return 记录的集合
     */
    List<T> selectList(QueryWrapper queryWrapper);

    /**
     * 插入一条记录
     * @param t 待带入的记录
     * @return 插入成功返回true，失败返回false
     */
    boolean insert(T t);

    /**
     * 批量插入
     * @param list 待插入的记录集合
     * @return 插入成功的记录条数
     */
    int insertBath(List<T> list);

    /**
     * 根据id删除一条记录
     * @param id 记录的Id编号
     * @return 是否删除成功。
     */
    boolean deleteById(Long id);
}

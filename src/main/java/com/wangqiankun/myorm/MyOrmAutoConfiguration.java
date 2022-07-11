package com.wangqiankun.myorm;

import com.wangqiankun.myorm.executor.MsSqlDdlExecutor;
import com.wangqiankun.myorm.executor.MySqlDdlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述:
 *
 * @author 王乾坤
 * @version 1.0
 * @date 2022/6/29 11:44
 */
@Slf4j
@Configuration
public class MyOrmAutoConfiguration {
    @Bean
    @ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
    public MySqlDdlExecutor mySqlDdlExecutor() {
        log.info("MySqlDdlExecutor加载");
        return new MySqlDdlExecutor();
    }

    @Bean
    @ConditionalOnClass(name = "com.microsoft.sqlserver.jdbc.SQLServerDriver")
    public MsSqlDdlExecutor msSqlDdlExecutor() {
        log.info("MsSqlDdlExecutor加载");
        return new MsSqlDdlExecutor();
    }

}

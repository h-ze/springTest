package com.hz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfig {

    @Scope("prototype") //prototype 原型 singleton 单例
    public Calendar getCalendar(){
        return Calendar.getInstance();
    }

    /**
     * 配置线程池
     * @return 返回ExecutorService对象
     */
    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
    //executorService.execute(new EmailRunnable(employee));

    public static Boolean isOpenRedis(){
        return false;
    }
}

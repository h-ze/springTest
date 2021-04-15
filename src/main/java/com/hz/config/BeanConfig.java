package com.hz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Calendar;

@Configuration
public class BeanConfig {

    @Scope("prototype") //prototype 原型 singleton 单例
    public Calendar getCalendar(){
        return Calendar.getInstance();
    }

    public static Boolean isOpenRedis(){
        return false;
    }
}

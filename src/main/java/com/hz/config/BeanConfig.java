package com.hz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Calendar;

@Configuration
public class BeanConfig {

    @Scope("prototype") //prototype 多例 singleton 单例
    public Calendar getCalendar(){
        return Calendar.getInstance();
    }
}

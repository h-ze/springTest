package com.hz.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 测试openfeign 主要测试内容为indexController中的方法提供给springemails中的indexController类使用
 */
//@SpringBootApplication
@EnableDiscoveryClient
@SpringBootApplication(/*exclude= {DataSourceAutoConfiguration.class}*/)
@MapperScan("com.hz.book.dao")
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

}

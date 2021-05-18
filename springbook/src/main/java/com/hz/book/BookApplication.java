package com.hz.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 测试openfeign
 */
//@SpringBootApplication
@EnableEurekaClient
@SpringBootApplication(/*exclude= {DataSourceAutoConfiguration.class}*/)
@MapperScan("com.hz.book.dao")
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

}

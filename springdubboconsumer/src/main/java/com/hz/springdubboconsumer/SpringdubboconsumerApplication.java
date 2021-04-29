package com.hz.springdubboconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SpringdubboconsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringdubboconsumerApplication.class, args);
    }

}

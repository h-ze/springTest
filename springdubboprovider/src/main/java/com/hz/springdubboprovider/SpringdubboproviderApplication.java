package com.hz.springdubboprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SpringdubboproviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringdubboproviderApplication.class, args);
    }

}

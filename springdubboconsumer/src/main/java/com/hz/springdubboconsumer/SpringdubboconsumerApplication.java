package com.hz.springdubboconsumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hz.springdubbointerface.service.UserInterfaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SpringdubboconsumerApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        ConfigurableApplicationContext context = SpringApplication.run(SpringdubboconsumerApplication.class, args);
    }

    @Component
    public class UserRpcServiceTest implements CommandLineRunner {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Reference(url = "dubbo://localhost:20880",version = "${dubbo.consumer.UserInterface.version}")
        private UserInterfaces userRpcService;

        @Override
        public void run(String... args) throws Exception {
            logger.info("kaisi");
            logger.info("测试："+userRpcService);
            userRpcService.getUser();
            logger.info("[run][发起一次 Dubbo RPC 请求，获得用户为({})");
        }

    }
}

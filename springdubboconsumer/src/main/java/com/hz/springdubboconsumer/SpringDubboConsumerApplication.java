package com.hz.springdubboconsumer;

import com.hz.springdubbointerface.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDubbo
public class SpringDubboConsumerApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        ConfigurableApplicationContext context = SpringApplication.run(SpringDubboConsumerApplication.class, args);
    }

    @Component
    public static class UserRpcServiceTest implements CommandLineRunner {

        private final Logger logger = LoggerFactory.getLogger(getClass());

<<<<<<< HEAD:springdubboconsumer/src/main/java/com/hz/springdubboconsumer/SpringdubboconsumerApplication.java
        @Reference(url = "dubbo://localhost:20880",version = "${dubbo.consumer.UserInterface.version}")
        private UserInterfaces userRpcService;
=======
        @Reference(/*url = "dubbo://localhost:20882",*/version = "${dubbo.consumer.UserInterface.version}")
        private UserService userRpcService;
>>>>>>> e1674c560ed022b51bfa139a29cd38b8f4f35c07:springdubboconsumer/src/main/java/com/hz/springdubboconsumer/SpringDubboConsumerApplication.java

        @Override
        public void run(String... args) {
            logger.info("测试："+userRpcService);
            userRpcService.setUser();
            logger.info("[run][发起一次 Dubbo RPC 请求，获得用户为({})");
        }

    }
}

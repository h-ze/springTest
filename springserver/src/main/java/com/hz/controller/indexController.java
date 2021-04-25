package com.hz.controller;

import com.hz.demo.entity.ResponseMessageWithoutException;
import com.hz.test.MyClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.Date;

import static com.hz.demo.entity.ResponseMessageWithoutException.errorResult;
import static com.hz.demo.entity.ResponseMessageWithoutException.successResult;


/**
 * 测试websocket连接
 */
@RestController
@RequestMapping("index")
@Slf4j
public class indexController {
    /*@Autowired
    SimpMessagingTemplate simpMessagingTemplate;*/

    @PostMapping("/sendMessage")
    public ResponseMessageWithoutException<String> sendMessage(@RequestParam String message, @RequestParam String token){
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            MyClient client = new MyClient();
            container.connectToServer(client, new URI("ws://localhost:8082/springboot/websocket/zhangsan?token="+token));
            client.send("客户端发送消息:" + message);
            return successResult(0,"发送成功","测试websocket成功");
        }catch (Exception e){
            e.printStackTrace();
            return errorResult(0,"测试websocket失败");
        }
    }

}
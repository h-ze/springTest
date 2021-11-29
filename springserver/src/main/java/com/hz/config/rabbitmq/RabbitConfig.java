package com.hz.config.rabbitmq;

import com.hz.demo.entity.MailConstants;
import com.hz.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//本项目主要使用定时器的方式进行rabbitmq的调用
/**
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitConfig {
    public final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
    //@Autowired
    ;
    //@Autowired
    //MailSendLogService mailSendLogService;

    @Autowired
    EmailService emailService;

    @Bean
    RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //生产者推送消息的消息确认调用回调函数
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            logger.info("相关数据 data: ={}",data);
            logger.info("确认情况 ack: ={}",ack);
            logger.info("原因 cause: ={}",cause);

            logger.info("test");
            String msgId = data.getId();
            if (ack) {
                logger.info(msgId + ":消息发送成功");
                //emailService.updateEmailStatus(Integer.valueOf(msgId),1);
                //mailSendLogService.updateMailSendLogStatus(msgId, 1);//修改数据库中的记录，消息投递成功
            } else {
                logger.info(msgId + ":消息发送失败");
            }
        });
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingKey) ->{
                System.out.println("ReturnCallback:     "+"消息："+msg);
                System.out.println("ReturnCallback:     "+"回应码："+repCode);
                System.out.println("ReturnCallback:     "+"回应信息："+repText);
                System.out.println("ReturnCallback:     "+"交换机："+exchange);
                System.out.println("ReturnCallback:     "+"路由键："+routingKey);
                logger.info("消息return发送");
        });
        return rabbitTemplate;
    }

    // 配置一个工作模型队列
    @Bean
    Queue mailQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // 支持持久化
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true);
    }

    // 配置一个测试工作模型队列 无实际意义
    @Bean
    Queue testQueue() {
        // 支持持久化
        return new Queue("test", true);

    }

    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    //绑定测试交换机和测试队列
    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

}

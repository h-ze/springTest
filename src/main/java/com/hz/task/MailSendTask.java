package com.hz.task;

import com.hz.config.BeanConfig;
import com.hz.entity.Employee;
import com.hz.entity.MailConstants;
import com.hz.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MailSendTask {
    /*@Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    EmployeeService employeeService;*/

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void mailResendTask() {
        SimpleDateFormat sd  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = sd.format(new Date());
        log.info("定时器任务: "+date);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1");
        //rabbitTemplate.convertAndSend(MailConstants.MAIL_QUEUE_NAME, new User()/*, correlationData*/);
        //User user = new User();
        //user.setName("test");
        Employee employee = new Employee();
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME,employee, new CorrelationData("1"));
        //rabbitTemplate.convertAndSend(MailConstants.MAIL_QUEUE_NAME, "测试work模型:测试RabbitMq"/*MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(mailSendLog.getMsgId())*/);
        /*List<MailSendLog> logs = mailSendLogService.getMailSendLogsByStatus();
        if (logs == null || logs.size() == 0) {
            return;
        }
        logs.forEach(mailSendLog->{
            if (mailSendLog.getCount() >= 3) {
                mailSendLogService.updateMailSendLogStatus(mailSendLog.getMsgId(), 2);//直接设置该条消息发送失败
            }else{
                mailSendLogService.updateCount(mailSendLog.getMsgId(), new Date());
                Employee emp = employeeService.getEmployeeById(mailSendLog.getEmpId());
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(mailSendLog.getMsgId()));
            }
        });*/
    }
}

package com.hz.email;

import com.hz.demo.entity.Employee;
import com.hz.demo.entity.MailConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
@Slf4j
public class MailReceiver {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    StringRedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException, MessagingException {
        log.info("message: ={}",message);
        log.info("channel: ={}",channel);
        Employee employee = (Employee) message.getPayload();
        /*MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            log.info(msgId + ":消息已经被消费");
            channel.basicAck(tag, false);//确认消息已消费
            return;
        }*/
        //sendEmail(employee);
    }

    private void sendEmail(Employee employee) throws MessagingException, UnsupportedEncodingException {
        //构造SMTP邮件服务器的基本环境
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        //构造邮件

        List<File> fileList = new ArrayList<>();
        fileList.add(new File("C:\\Users\\Admin\\Desktop\\Phantom权限标志位整理.xlsx"));

        List<File> picList = new ArrayList<>();
        picList.add(new File("C:\\Users\\Admin\\Desktop\\捕获.PNG"));
        picList.add(new File("C:\\Users\\Admin\\Desktop\\捕获.PNG"));

        MimeMessage mimeMessage = saveMessage(session,"1102211390@qq.com","1554752374@qq.com",null,"邮件主题",employee,fileList,picList);
        //发送邮件
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "1102211390@qq.com", /*"iskpdrftnlgohbih",*/"kuwvhzyxkknujigi");
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());//发送邮件，第二个参数为收件人
        transport.close();
    }

    /**
     * 构建主体内容
     * @param list 主体图片集合
     * @return
     */
    private String setEmailContent(List<String> list,Employee employee){
        Context context = new Context();
        context.setVariable("name", "test");
        context.setVariable("posName", "test");
        context.setVariable("joblevelName", "test");
        context.setVariable("departmentName", "test");
        context.setVariable("test","test");
        String mail = templateEngine.process("mail", context);
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : list) {
            stringBuilder =stringBuilder.append("<img src='cid:"+name+"'/>");
        }
        return "<h1>Hello大家好，这是一封测试邮件"+stringBuilder.toString()+"</h1>"+mail;
    }

    /**
     * 设置发送内容，包含附件等复杂内容
     * @param session Session对象
     * @param fromEmail 发送邮件的邮箱
     * @param toEmail 发送至的邮箱
     * @param ccEmail 抄送至的邮箱
     * @param subject 邮件主题
     * @param files 附件内容
     * @param headPic 正文的图片
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private MimeMessage saveMessage(Session session,String fromEmail, String toEmail, String ccEmail, String subject,Employee employee,List<File> files,List<File> headPic) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipients(javax.mail.Message.RecipientType.TO, toEmail);//设置收信人
        if (ccEmail!=null){
            mimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, ccEmail);//抄送
        }
        mimeMessage.setFrom(fromEmail);//邮件发送人
        mimeMessage.setSubject(subject);//邮件主题

        MimeMultipart mixed = new MimeMultipart("mixed");
        mimeMessage.setContent(mixed);//设置整封邮件的MIME消息体为混合的组合关系

        for (File file : files) {
            addAttach(mixed,file);
        }
        MimeBodyPart content = new MimeBodyPart();//创建邮件正文
        mixed.addBodyPart(content);//将正文添加到消息体中

        MimeMultipart bodyMimeMultipart = new MimeMultipart("related");//设置正文的MIME类型
        content.setContent(bodyMimeMultipart);//将bodyMimeMultipart添加到正文消息体中

        MimeBodyPart bodyPart = new MimeBodyPart();//正文的HTML部分
        List<String> list = new ArrayList<>();
        for (File file : headPic) {
            MimeBodyPart picPart = new MimeBodyPart();//正文的图片部分
            DataHandler dataHandler = new DataHandler(new FileDataSource(file.getPath()));
            picPart.setDataHandler(dataHandler);
            String id = UUID.randomUUID().toString().replace("-","") + ".png";
            picPart.setContentID(id);
            list.add(id);
            bodyMimeMultipart.addBodyPart(picPart);
        }

        bodyPart.setContent(setEmailContent(list,employee),"text/html;charset=utf-8");

        //将正文的HTML和图片部分分别添加到bodyMimeMultipart中
        bodyMimeMultipart.addBodyPart(bodyPart);

        mimeMessage.saveChanges();
        return mimeMessage;
    }

    private void addAttach(MimeMultipart mixed, File file) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart attach2 = new MimeBodyPart();//创建附件2

        mixed.addBodyPart(attach2);//将附件二添加到MIME消息体中

        //附件二的操作与附件一类似，这里就不一一注释了
        FileDataSource fds2 = new FileDataSource(file/*new File("C:\\Users\\sang\\Desktop\\博客笔记.xlsx")*/);
        DataHandler dh2 = new DataHandler(fds2);
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText(file.getName()));//设置文件名时，如果有中文，可以通过MimeUtility类中的encodeText方法进行编码，避免乱码
    }

    //收到消息，发送邮件
        /*MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            //helper.setTo(employee.getEmail());
            helper.setTo("1554752374@qq.com");
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("入职欢迎");
            helper.setSentDate(new Date());
            Context context = new Context();
            *//*context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            *//*
            context.setVariable("name", "test");
            context.setVariable("posName", "test");
            context.setVariable("joblevelName", "test");
            context.setVariable("departmentName", "test");
            context.setVariable("test","test");
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(msg);
            *//*redisTemplate.opsForHash().put("mail_log", msgId, "javaboy");
            channel.basicAck(tag, false);
            log.info(msgId + ":邮件发送成功");*//*
        } catch (MessagingException e) {
           *//* channel.basicNack(tag, false, true);
            e.printStackTrace();
            log.error("邮件发送失败：" + e.getMessage());*//*
        }*/
}

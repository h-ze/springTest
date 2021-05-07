package com.hz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("email")
public class EmailController {

    /*@Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;

    @PostMapping("/sendEmail")
    public void handler() throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipients(Message.RecipientType.TO, "1554752374@qq.com");//设置收信人
        //mimeMessage.addRecipients(Message.RecipientType.CC, "222@qq.com");//抄送
        mimeMessage.setFrom("1102211390@qq.com");//邮件发送人
        mimeMessage.setSubject("测试邮件主题");//邮件主题
        mimeMessage.setContent("Hello,这是一封测试邮件", "text/html;charset=utf-8");//正文
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "1102211390@qq.com", *//*"iskpdrftnlgohbih"*//*"kuwvhzyxkknujigi");
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());//发送邮件，第二个参数为收件人
        transport.close();
    }*/
}

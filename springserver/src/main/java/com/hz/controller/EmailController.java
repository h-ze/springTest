package com.hz.controller;

import com.hz.demo.entity.Email;
import com.hz.demo.entity.ResponseResult;
import com.hz.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping
    public ResponseResult<String> resendEmail(String email){
        Email emailById = emailService.getEmail(email);
        int i;
        if (emailById.getStatus() !=0){
            i = emailService.updateEmailStatus(emailById, 2);
        }else {
            return new ResponseResult<>(100001, "账号已激活,无需再次激活", "激活成功");
        }
        if (i>0){
            return new ResponseResult<>(100000,"邮件已重新发送,请稍后","发送成功");
        }
        return new ResponseResult<>(100000,"邮件发送失败,请重新发送","发送失败");
    }

}

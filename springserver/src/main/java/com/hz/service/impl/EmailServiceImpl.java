package com.hz.service.impl;

import com.hz.dao.EmailDao;
import com.hz.demo.entity.Email;
import com.hz.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("emailService")
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailDao emailDao;

    @Override
    public List<Email> getUnactivatedEmails(int status) {
        return emailDao.getEmailList(status);
    }

    @Override
    public int updateEmailStatus(Integer emailId,int status) {
        return emailDao.updateEmailStatus(emailId, status);
    }

    @Override
    public Email getEmail(String email) {
        return emailDao.getEmail(email);
    }
}

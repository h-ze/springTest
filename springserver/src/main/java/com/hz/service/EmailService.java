package com.hz.service;

import com.hz.demo.entity.Email;

import java.util.List;

public interface EmailService {

    List<Email> getUnactivatedEmails(int status);

    int updateEmailStatus(Integer emailId,int status);

    Email getEmail(String email);
}
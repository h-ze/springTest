package com.hz.service;

import com.hz.demo.entity.Email;

import java.util.List;

public interface EmailService {
    List<Email> getUnactivatedEmails();
}

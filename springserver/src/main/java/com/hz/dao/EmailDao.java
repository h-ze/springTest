package com.hz.dao;

import com.hz.demo.entity.Email;

import java.util.List;

public interface EmailDao {

    int addEmailMessage(Email email);

    Email getEmail(String email);

    List<Email> getEmailList(int status);

    int updateEmailStatus(int emailId,int status);

}

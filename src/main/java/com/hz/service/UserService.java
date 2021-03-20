package com.hz.service;

import com.hz.entity.Role;
import com.hz.entity.User;

import java.util.List;

public interface UserService {
    int save(User user);
    List<User> findAll();
    User getUser(String username);
    User findRolesByUsername(String username);

}

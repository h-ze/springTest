package com.hz.dao;

import com.hz.entity.Role;
import com.hz.entity.User;

import java.util.List;


public interface UserDAO {
    int save(User user);
    List<User> findAll();
    User getUser(String username);

    User findRolesByUsername(String username);

}

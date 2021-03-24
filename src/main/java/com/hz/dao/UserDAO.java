package com.hz.dao;

import com.hz.entity.User;

import java.util.List;


public interface UserDAO {
    int save(User user);
    List<User> findAll();
    User getUser(String username);
    User getUserById(String userId);
    User findRolesByUsername(String username);
    int deleteUser(String userId);
    int deleteUserByOwner(String userId,String password);
    int updateUser(User user);


}

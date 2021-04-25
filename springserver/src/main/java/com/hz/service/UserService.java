package com.hz.service;


import com.hz.demo.entity.User;
import com.hz.demo.entity.UserRoles;

import java.util.List;

public interface UserService {
    int save(User user, UserRoles userRoles);

    int addUserRoles(UserRoles userRoles);

    List<User> findAll();

    User getUser(String username);

    User getUserByUserId(String userId);

    User findRolesByUsername(String username);

    int deleteUser(String userId,String password);

    int deleteUser(String userId);

    int updateUserPassword(User user);

}

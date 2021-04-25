package com.hz.dao;


import com.hz.demo.entity.User;
import com.hz.demo.entity.UserRoles;

import java.util.List;


public interface UserDAO {
    int save(User user);
    int addUserRoles(UserRoles userRoles);
    List<User> findAll();
    User getUser(String username);
    User getUserById(String userId);
    User findRolesByUsername(String username);
    int deleteUser(String userId);
    int deleteUserByOwner(String userId,String password);
    int updateUser(User user);


}

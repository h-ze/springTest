package com.hz.service.impl;

import com.hz.dao.UserDAO;
import com.hz.entity.User;
import com.hz.entity.UserRoles;
import com.hz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Override
    public int save(User user,UserRoles userRoles) {
        //user.setId(UUID.randomUUID().toString().replace("-",""));
        user.setId(0);
        if (userRoles!=null){
            userRoles.setId(0);
            userDAO.addUserRoles(userRoles);
        }

        return userDAO.save(user);
    }

    @Override
    public int addUserRoles(UserRoles userRoles) {
        userRoles.setRoleId(0);

        return userDAO.addUserRoles(userRoles);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User getUser(String username) {
        User user = userDAO.getUser(username);

        if(user!=null){
            return user;
        }else {
            return null;
        }
        //throw new RuntimeException("认证失败");
    }

    @Override
    public User getUserByUserId(String userId) {
        User user = userDAO.getUserById(userId);
        if(user!=null){
            return user;
        }else {
            return null;
        }
    }

    @Override
    public User findRolesByUsername(String username) {
        return userDAO.findRolesByUsername(username);
    }

    @Override
    public int deleteUser(String userId, String password) {
        int i = userDAO.deleteUserByOwner(userId, password);
        return i;
    }

    @Override
    public int deleteUser(String userId) {
        int i = userDAO.deleteUser(userId);
        return i;
    }

    @Override
    public int updateUserPassword(User user) {
        int i = userDAO.updateUser(user);
        return i;
    }

}

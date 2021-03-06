package com.hz.service.impl;

import com.hz.dao.UserDAO;
import com.hz.entity.Role;
import com.hz.entity.User;
import com.hz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Override
    public int save(User user) {
        //user.setId(UUID.randomUUID().toString().replace("-",""));
        user.setId(0);

        return userDAO.save(user);
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
    public User findRolesByUsername(String username) {
        return userDAO.findRolesByUsername(username);
    }

}

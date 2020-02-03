package com.niuge.bimcat.service.user;

import com.niuge.bimcat.pojo.User;

public interface UserService {

    public User findByName(String name);

    public User findById(Integer id);
}

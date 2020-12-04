package com.demo.crm.settings.service;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.daomian.User;

import java.util.ArrayList;

public interface UserService {

    public User login(String user,String psw,String ip) throws LoginException;

    ArrayList<User> getUserList();
}

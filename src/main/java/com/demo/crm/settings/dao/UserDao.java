package com.demo.crm.settings.dao;

import com.demo.crm.settings.daomian.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao {


    public User selectUserLogin(@Param("user") String user,@Param("psw") String psw);

    public List<User> getUserList();

    String selectByIdName(String name);
}

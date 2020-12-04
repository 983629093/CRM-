package com.demo.crm.workbench.dao;


import com.demo.crm.settings.daomian.User;
import com.demo.crm.workbench.daomian.Clue;

import java.util.List;

public interface ClueDao {


    List<User> getUserList();

    Clue detail(String id);

    Clue selectAllById(String clueId);

    int delete(String clueId);
}

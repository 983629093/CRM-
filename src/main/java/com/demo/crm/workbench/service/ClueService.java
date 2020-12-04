package com.demo.crm.workbench.service;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.workbench.daomian.Clue;
import com.demo.crm.workbench.daomian.Tran;

import java.util.List;

public interface ClueService {
    List<User> getUserList();

    Clue detail(String id);

    boolean castClue(String clueId, Tran tran, String createBy);
}

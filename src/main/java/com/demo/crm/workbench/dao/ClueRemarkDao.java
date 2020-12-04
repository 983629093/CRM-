package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.daomian.ClueRemark;

import java.util.ArrayList;

public interface ClueRemarkDao {

    ArrayList<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}

package com.demo.crm.workbench.service.impl;

import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.workbench.dao.CustomerDao;
import com.demo.crm.workbench.daomian.Customer;
import com.demo.crm.workbench.service.CustomerServics;

import java.util.ArrayList;

public class CustomerServiceImp implements CustomerServics {
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public ArrayList<String> LikeselectName(String name) {
       ArrayList<String> list= customerDao.LikeselectName(name);
        return list;
    }
}

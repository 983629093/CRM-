package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.daomian.Customer;

import java.util.ArrayList;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    ArrayList<String> LikeselectName(String name);
}

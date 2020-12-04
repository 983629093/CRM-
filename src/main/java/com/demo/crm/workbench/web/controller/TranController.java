package com.demo.crm.workbench.web.controller;

import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.daomian.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.settings.service.impl.UserServiceImpl;
import com.demo.crm.utils.PrintJson;
import com.demo.crm.utils.ServiceFactory;
import com.demo.crm.workbench.daomian.Customer;
import com.demo.crm.workbench.service.CustomerServics;
import com.demo.crm.workbench.service.impl.CustomerServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TranController extends HttpServlet{
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=req.getServletPath();
        System.out.println(path);
        if ("/workbench/transaction/add.do".equals(path)){
            add(req,resp);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
           getCustomerName(req,resp);
            System.out.println("接受到请求");
        }
    }

    //查询客户
    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {
        String name=req.getParameter("name");
        CustomerServics customerServics= (CustomerServics) ServiceFactory.getService(new CustomerServiceImp());
        ArrayList<String> list = customerServics.LikeselectName(name);
        PrintJson.printJsonObj(resp,list);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        ArrayList<User> list=userService.getUserList();
        req.setAttribute("ulist",list);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }
}

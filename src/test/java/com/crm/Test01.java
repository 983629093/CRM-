package com.crm;

import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.daomian.User;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.MD5Util;
import com.demo.crm.utils.SqlSessionUtil;

import java.io.File;
import java.util.Date;

public class Test01 {
    public static void main(String[] args) {
        File f=new File("src\\main\\resources\\jdbc.properties");
        System.out.println(f.length());
    }
}

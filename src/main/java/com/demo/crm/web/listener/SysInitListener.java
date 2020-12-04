package com.demo.crm.web.listener;

import com.demo.crm.settings.daomian.DicValue;
import com.demo.crm.settings.service.DicService;
import com.demo.crm.settings.service.impl.DicServiceImpl;
import com.demo.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext application= servletContextEvent.getServletContext();
        DicService dicService= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map= dicService.getAll();
        //将取得的数据存入字典
        for(String str : map.keySet()){
            application.setAttribute(str,map.get(str));
        }

        //在将配置文件存入上下文 交易里的键值对
        ResourceBundle resourceBundle=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> enumeration= resourceBundle.getKeys();
        HashMap<String,String> map1=new HashMap<String, String>();
        while (enumeration.hasMoreElements()){
            String key=enumeration.nextElement();
            String value=resourceBundle.getString(key);
            map1.put(key,value);
        }
        application.setAttribute("pMap",map1);

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

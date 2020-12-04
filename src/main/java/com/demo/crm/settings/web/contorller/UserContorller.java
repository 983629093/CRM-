package com.demo.crm.settings.web.contorller;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.settings.service.impl.UserServiceImpl;
import com.demo.crm.utils.MD5Util;
import com.demo.crm.utils.PrintJson;
import com.demo.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserContorller extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }
    //登陆方法
    public  void login(HttpServletRequest request,HttpServletResponse response){
        String  user=request.getParameter("user");
        String  psw=request.getParameter("psw");
        String  ip=request.getRemoteAddr();
        System.out.println("ip===="+ip);
        //将密码转化为MD5格式
        psw=MD5Util.getMD5(psw);

        //开始调用业务层 处理事务  此处使用动态代理

        UserService us= (UserService)ServiceFactory.getService(new UserServiceImpl());

        try {
            User user1=us.login(user,psw,ip);
            //执行到这里表示代码登录成功
            request.getSession().setAttribute("user",user1);
            PrintJson.printJsonFlag(response,true);

        }catch (Exception e){
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*
                {"success":true,"msg":?}
             */
            String msg = e.getMessage();
            System.out.println(msg);
            /*
                我们现在作为contoller，需要为ajax请求提供多项信息
                可以有两种手段来处理：
                    （1）将多项信息打包成为map，将map解析为json串
                    （2）创建一个Vo
                            private boolean success;
                            private String msg;
                    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                    如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了

             */

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
            System.out.println("tan");
            String age;
            System.out.println("友杰");
            String name;


        }

    }
}

package com.demo.crm.workbench.web.controller;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.utils.*;
import com.demo.crm.vo.VoListData;
import com.demo.crm.workbench.daomian.Activity;
import com.demo.crm.workbench.service.ActivityService;
import com.demo.crm.workbench.service.impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String path=req.getServletPath();
        System.out.println(path);
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(resp);
        }else if("/workbench/activity/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/activity/selectActivity.do".equals(path)){
            selectActivity(req,resp);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/activity/selectListAndActivity.do".equals(path)){
            selectListAndActivity(req,resp);
        }else if("/workbench/activity/updateActivity.do".equals(path)){
            updateActivity(req,resp);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }
    }




    //获得用户名列表
    public void getUserList(HttpServletResponse response){
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //调用服务层执行查询方法
        List<User> list=activityService.getUserList();
        //使用json格式 返回给网页
        PrintJson.printJsonObj(response,list);
        System.out.println("返回list");
    }

    //添加用户信息
    public  void  save(HttpServletRequest request,HttpServletResponse response){
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //获取数据
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String starDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();

        Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(starDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        //调用业务层

        boolean bool= activityService.sava(activity);
        //返回响应
        PrintJson.printJsonFlag(response,bool);
    }


    //查询页面的表格数据
    public void selectActivity(HttpServletRequest request,HttpServletResponse response) {
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");
        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");

        //计算页数关系

        int num1=Integer.valueOf(pageNo);
        int num2=Integer.valueOf(pageSize);
        int num3=(num1-1)*num2;


        HashMap<String,Object> map =new HashMap();
        map.put("pageNo",num3);
        map.put("pageSize",num2);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        //交给业务层


        VoListData<Activity> vo= activityService.selectActivity(map);
        PrintJson.printJsonObj(response,vo);
    }

    //删除activity
    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String data[]=req.getParameterValues("id");
        boolean bool= activityService.delete(data);
        PrintJson.printJsonFlag(resp,bool);
    }

    public void selectListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id=req.getParameter("id");
        HashMap<String,Object> map=activityService.selectListAndActivity(id);
        PrintJson.printJsonObj(resp,map);
    }

    //更新市场活动
    private void updateActivity(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id=req.getParameter("id");
        String onwer=req.getParameter("owner");
        String name=req.getParameter("name");
        String startDate=req.getParameter("startTime");
        String endDate=req.getParameter("endTime");
        String cost=req.getParameter("cost");
        String describe=req.getParameter("describe");
        String editTime=DateTimeUtil.getSysTime();
        User user= (User) req.getSession().getAttribute("user");
        String editBy= user.getId() ;

       Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(onwer);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(describe);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        boolean bool= activityService.updateActivity(activity);
        PrintJson.printJsonFlag(resp,bool);
    }

    //
    private void detail(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id=req.getParameter("id");
        Activity activity = activityService.detail(id);
        req.setAttribute("a",activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }
}

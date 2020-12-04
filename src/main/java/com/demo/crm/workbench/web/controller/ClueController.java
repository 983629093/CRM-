package com.demo.crm.workbench.web.controller;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.utils.*;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ClueDao;
import com.demo.crm.workbench.daomian.Activity;
import com.demo.crm.workbench.daomian.Clue;
import com.demo.crm.workbench.daomian.ClueActivityRelation;
import com.demo.crm.workbench.daomian.Tran;
import com.demo.crm.workbench.service.ActivityService;
import com.demo.crm.workbench.service.ClueActivityRelationService;
import com.demo.crm.workbench.service.ClueService;
import com.demo.crm.workbench.service.impl.ActivityServiceImpl;
import com.demo.crm.workbench.service.impl.ClueActivityRelationServiceImpl;
import com.demo.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=req.getServletPath();
        System.out.println(path);
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/clue/detailGetListActivityById.do".equals(path)){
            detailGetListActivityById(req,resp);
        }else if("/workbench/clue/deleteRelation.do".equals(path)){
            deleteRelation(req,resp);
        }else if("/workbench/clue/selectActivityByName.do".equals(path)){
            selectActivityByName(req,resp);
        }else if("/workbench/clue/addGuanLian.do".equals(path)){
            addGuanLian(req,resp);
        }else if("/workbench/clue/selectAllActivityByName.do".equals(path)){
            selectAllActivityByName(req,resp);
        }else if("/workbench/clue/castClue.do".equals(path)){
            castClue(req,resp);
        }
    }
    //转换线索
    private void castClue(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String flag=req.getParameter("flag");
        String clueId = req.getParameter("clueId");
        Tran tran=null;
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        //如果需要交易
        if ("true".equals(flag)){
            tran=new Tran();
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            tran.setId(id);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean bool=cs.castClue(clueId,tran,createBy);
        if (bool){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    //查询所有活动市场
    private void selectAllActivityByName(HttpServletRequest req, HttpServletResponse resp) {
        String  name=req.getParameter("name");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ArrayList<Activity> list=activityService.selectAllActivityByName(name);
        PrintJson.printJsonObj(resp,list);
    }

    //添加关联关系
    private void addGuanLian(HttpServletRequest req, HttpServletResponse resp) {
        String cid=req.getParameter("cid");
        String aid[]=req.getParameterValues("aid");
        try {
            ClueActivityRelationService clue= (ClueActivityRelationService) ServiceFactory.getService(new ClueActivityRelationServiceImpl());
            clue.addGuanLian(cid,aid);
            //如果执行完以上操作没有报错则执行此步骤
            PrintJson.printJsonFlag(resp,true);
        }catch (Exception e){
            PrintJson.printJsonFlag(resp,false);
        }



    }

    private void selectActivityByName(HttpServletRequest req, HttpServletResponse resp) {
        String name=req.getParameter("name");
        String id=req.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ArrayList<Activity> list = activityService.selectActivityByName(id,name);
        PrintJson.printJsonObj(resp,list);
    }

    //删除关系表
    private void deleteRelation(HttpServletRequest req, HttpServletResponse resp) {
        String id= req.getParameter("id");
        ClueActivityRelationService activityRelationService= (ClueActivityRelationService)ServiceFactory.getService(new ClueActivityRelationServiceImpl());
        boolean bool = activityRelationService.deleteRelation(id);
        PrintJson.printJsonFlag(resp,bool);
    }

    //通过潜在用户id查询有关市场活动
    private void detailGetListActivityById(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("开始执行");
        String id= req.getParameter("id");
        System.out.println(id);
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ArrayList<Activity> list = activityService.detailGetListActivityById(id);
        PrintJson.printJsonObj(resp,list);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id=req.getParameter("id");
        Clue clue= clueService.detail(id);
        req.setAttribute("c",clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> list = clueService.getUserList();
        PrintJson.printJsonObj(resp,list);

    }
}

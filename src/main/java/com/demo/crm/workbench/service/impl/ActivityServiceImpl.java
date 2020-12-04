package com.demo.crm.workbench.service.impl;
import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.daomian.User;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.vo.VoListData;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ActivityRemarkDao;
import com.demo.crm.workbench.daomian.Activity;
import com.demo.crm.workbench.service.ActivityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    /**
     * 查询用户名
     * @return List<User>集合
     */
    public List<User> getUserList() {
//        UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        List<User> list= userDao.getUserList();
        return list;
    }


    public boolean sava(Activity activity) {
//        ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        int num=activityDao.save(activity);
        boolean bool=true;
        if (num!=1){
            bool=false;
        }
        return  bool;
    }


    public VoListData<Activity> selectActivity(HashMap<String,Object> map) {
        VoListData<Activity> voListData=new VoListData<Activity>();
        //调用Dao层获取数据
//        ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        int pageNumber= activityDao.selectPageNumber(map);
        List<Activity> list= activityDao.selectPageData(map);
        voListData.setPage(pageNumber);
        voListData.setList(list);
        return voListData;
    }


    public boolean delete(String[] data) {
//        ActivityRemarkDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        //得到要检查的行数
        int num1=activityRemarkDao.selectActivityRemark(data);
        int num2=activityRemarkDao.deleteActivityRemarkId(data);
        if (num1!=num2){
            return false;
        }
        //删除ActivityId
//        ActivityDao activityDao1=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        int num3=activityDao.delectActivityId(data);
        if (num3!=data.length){
            return false;
        }
        return true;
    }

    public HashMap<String, Object> selectListAndActivity(String id) {
        //查询所有人
//        ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        String name= activityDao.selectUserId(id);
       //查询activity对象
        Activity activity= activityDao.selectActivity(id);
        HashMap<String,Object> map=new HashMap<String, Object>();
        map.put("name",name);
        map.put("activity",activity);
        return map;
    }

    //更新用户活动
    public boolean updateActivity(Activity activity) {
        String ownerId=activity.getOwner();
        String getOwner=userDao.selectByIdName(ownerId);
        activity.setOwner(getOwner);
        int num = activityDao.updateActivity(activity);
        if (num!=1){
            return false;
        }
        return true;
    }

    //查询详细信息表 返回Activity
    public Activity detail(String id) {

        return activityDao.detail(id);
    }

    public ArrayList<Activity> detailGetListActivityById(String id) {
        ArrayList<Activity>  list=activityDao.detailGetListActivityById(id);
        return list;
    }

    //通过模糊查询查找到除了显示在列表外的其他数据
    public ArrayList<Activity> selectActivityByName(String id,String name) {
        ArrayList<Activity> list =activityDao.selectActivityByName(id,name);
        return list;
    }

    public ArrayList<Activity> selectAllActivityByName(String name) {
        ArrayList<Activity> list=activityDao.selectAllActivityByName(name);
        return list;
    }
}

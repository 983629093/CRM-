package com.demo.crm.workbench.service;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.vo.VoListData;
import com.demo.crm.workbench.daomian.Activity;
import com.demo.crm.workbench.daomian.ActivityRemark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ActivityService {
    //查询用户列表
   public  List<User> getUserList();
    //保存添加的活动
    boolean sava(Activity activity);
    //查询活动列表
    VoListData<Activity> selectActivity(HashMap<String,Object> map);
    //删除
    boolean delete(String[] data);
    //查询用户id与mao集合
    HashMap<String, Object> selectListAndActivity(String id);

    boolean updateActivity(Activity activity);

    Activity detail(String id);

    ArrayList<Activity> detailGetListActivityById(String id);

    ArrayList<Activity> selectActivityByName(String id,String name);

    ArrayList<Activity> selectAllActivityByName(String name);
}

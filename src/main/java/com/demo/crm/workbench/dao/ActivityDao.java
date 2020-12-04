package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.daomian.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ActivityDao {
   public int save(Activity activity);


    int selectPageNumber(HashMap<String,Object> map);

    List<Activity> selectPageData(HashMap<String,Object> map);

    int delectActivityId(String[] data);


    //查询修改模态里的拥有者
    String selectUserId(String id);
    //查询修改模态对象
    Activity selectActivity(String id);

    int updateActivity(Activity activity);

    Activity detail(String id);

    ArrayList<Activity> detailGetListActivityById(String id);

    ArrayList<Activity> selectActivityByName(@Param("id") String id,@Param("name")  String name);

    ArrayList<Activity> selectAllActivityByName(String name);
}

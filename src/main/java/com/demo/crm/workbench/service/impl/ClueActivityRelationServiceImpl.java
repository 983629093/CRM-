package com.demo.crm.workbench.service.impl;

import com.demo.crm.exception.AddGuanLianException;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.workbench.dao.ClueActivityRelationDao;
import com.demo.crm.workbench.daomian.Activity;
import com.demo.crm.workbench.daomian.ClueActivityRelation;
import com.demo.crm.workbench.service.ClueActivityRelationService;

import java.util.ArrayList;

public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    ClueActivityRelationDao activityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    //删除关联
    public boolean deleteRelation(String id) {
        int num= activityRelationDao.deleteRelation(id);
        if (num!=1){
            return false;
        }
        return true;
    }

    //关联关系
    public void addGuanLian(String cid, String[] aid) throws AddGuanLianException {

        for (String id: aid){
            ClueActivityRelation clue=new ClueActivityRelation();
            clue.setId(UUIDUtil.getUUID());
            clue.setClueId(cid);
            clue.setActivityId(id);
            int num=activityRelationDao.addGuanLian(clue);
            if (num!=1){
                throw new AddGuanLianException("添加出错");
            }
        }

    }




}

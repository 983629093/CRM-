package com.demo.crm.settings.service.impl;

import com.demo.crm.settings.dao.DicTypeDao;
import com.demo.crm.settings.dao.DicValueDao;
import com.demo.crm.settings.daomian.DicType;
import com.demo.crm.settings.daomian.DicValue;
import com.demo.crm.settings.service.DicService;
import com.demo.crm.utils.ServiceFactory;
import com.demo.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    DicValueDao dicValueDao= SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    DicTypeDao dicTypeDao=SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map=new HashMap<String, List<DicValue>>();
        List<DicType> list = dicTypeDao.getTypeList();
        for (DicType dicType : list){
            String code=dicType.getCode();
            List<DicValue> dicValue= dicValueDao.getListByCode(code);
            map.put(code,dicValue);
        }
        return map;
    }
}

package com.demo.crm.settings.dao;

import com.demo.crm.settings.daomian.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}

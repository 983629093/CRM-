package com.demo.crm.workbench.dao;


import com.demo.crm.workbench.daomian.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int deleteRelation(String id);

    int addGuanLian(ClueActivityRelation clue);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}

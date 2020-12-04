package com.demo.crm.workbench.service;

import com.demo.crm.exception.AddGuanLianException;
import com.demo.crm.workbench.daomian.Activity;

import java.util.ArrayList;

public interface ClueActivityRelationService {
    boolean deleteRelation(String id);

    void addGuanLian(String cid, String[] aid) throws AddGuanLianException;

}

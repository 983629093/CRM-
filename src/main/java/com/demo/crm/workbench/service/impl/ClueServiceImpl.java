package com.demo.crm.workbench.service.impl;

import com.demo.crm.settings.daomian.User;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.workbench.dao.*;
import com.demo.crm.workbench.daomian.*;
import com.demo.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.List;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    public List<User> getUserList() {
        List<User> list = clueDao.getUserList();
        return list;
    }

    public Clue detail(String id) {
      Clue clue=  clueDao.detail(id);
        return clue;
    }
    //线索转换
    public boolean castClue(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
//        (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        System.out.println("clueId=="+clueId);
        Clue clue = clueDao.selectAllById(clueId);

//        (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        if (cus == null) {
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(clue.getAddress());
            cus.setWebsite(clue.getWebsite());
            cus.setPhone(clue.getPhone());
            cus.setOwner(clue.getOwner());
            cus.setNextContactTime(clue.getNextContactTime());
            cus.setName(company);
            cus.setDescription(clue.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(clue.getContactSummary());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1) {
                flag = false;
            }
        }
//        (3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(clue.getContactSummary());
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(con);
        if(count2!=1){
            flag = false;
        }

//        (4) 线索备注转换到客户备注以及联系人备注
        //查询拿到该线索的备注
        ArrayList<ClueRemark> clueRemarks=clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark:clueRemarks){
            //取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3!=1){
                flag = false;
            }

            //创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4!=1){
                flag = false;
            }
        }

//        (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        //遍历出每一条与市场活动关联的关联关系记录
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            //从每一条遍历出来的记录中取出关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            //创建 联系人与市场活动的关联关系对象 让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            //添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag = false;
            }
        }
        //(6)如果有创建交易需求，创建一条交易
        if(tran!=null){
            /*
                t对象在controller里面已经封装好的信息如下：
                    id,money,name,expectedDate,stage,activityId,createBy,createTime

                接下来可以通过第一步生成的c对象，取出一些信息，继续完善对t对象的封装

             */
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(cus.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(con.getId());
            //添加交易
            int count6 = tranDao.save(tran);
            if(count6!=1){
                flag = false;
            }

            //(7)如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(th);
            if(count7!=1){
                flag = false;
            }


        }

        //(8)删除线索备注
        for(ClueRemark clueRemark : clueRemarks){

            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag = false;
            }

        }

        //(9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){

                flag = false;

            }

        }

        //(10) 删除线索
        int count10 = clueDao.delete(clueId);
        if(count10!=1){
            flag = false;
        }
        return flag;
    }
}

package com.demo.crm.settings.service.impl;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.daomian.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;

public class UserServiceImpl implements UserService {
   private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

   /**
    * 判断账号密码是否合法
    * @param user  账号
    * @param psw  密码
    * @param ip   ip
    * @return    User对象
    */
   public User login(String user, String psw, String ip) throws LoginException {
      //调用dao层拿到该用户对象 没有就抛出异常
      User user1=userDao.selectUserLogin(user,psw);
      if (user1==null){
         System.out.println("账号密码错误");
        throw new LoginException("账号密码错误");
      }

      String nowtime= DateTimeUtil.getSysTime();
      String expireTime= user1.getExpireTime();
      if (expireTime.compareTo(nowtime)<0){
      throw new LoginException("该账号以过期");
      }

      if (Integer.valueOf(user1.getLockState())==0){
         throw new LoginException("该账号以被锁定");
      }

//      String userIp=user1.getAllowIps();
      //此人只能用该电脑登录
//      if(!userIp.contains(ip)){
//         throw new LoginException("IP受限");
//      }
      return user1;
   }

   public ArrayList<User> getUserList() {
     ArrayList<User> list= (ArrayList<User>) userDao.getUserList();
      return list;
   }
}

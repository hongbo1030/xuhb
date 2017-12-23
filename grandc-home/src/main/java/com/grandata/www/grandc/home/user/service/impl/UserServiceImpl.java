package com.grandata.www.grandc.home.user.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.grandata.www.grandc.common.exception.CustomerException;
import com.grandata.www.grandc.domain.user.UserBean;
import com.grandata.www.grandc.home.user.dao.UserDao;
import com.grandata.www.grandc.home.user.service.IUserService;

@Service("userservice")
public class UserServiceImpl implements IUserService {

  private static Logger logger = Logger.getLogger(UserServiceImpl.class);

  @Autowired
  private UserDao userDao;

  @Override
  public int checkUsernamePassword(String username, String password) {
    UserBean userBean = new UserBean();
    userBean.setUsername(username);
    userBean.setPassword(password);
    return userDao.checkUsernamePassword(userBean);
  }

  @Override
  public int checkUsername(String username) {
    return userDao.checkUsername(username);
  }

  @Override
  public int create(String username, String password) throws CustomerException {
    //step1.校验用户是否存在
    UserBean userBean = new UserBean();
    int icount = this.checkUsername(username);
    if(icount > 0) {
      throw new CustomerException(1001, "用户已存在");
    }
    //step2.创建用户
    userBean.setUsername(username);
    userBean.setPassword(password);
    userBean.setStatus(0);
    userBean.setDscp(null);
    // 返回值
    return userDao.create(userBean);
  }

  @Override
  public int modifypwd(String username, String password) throws CustomerException {
    //step1.校验用户是否存在
    int icount = this.checkUsername(username);
    if(icount == 0) {
      throw new CustomerException(1002, "用户不存在");
    }
    //step2.修改用户密码
    UserBean userBean = new UserBean();
    userBean.setUsername(username);
    userBean.setPassword(password);
    userBean.setStatus(0);
    userBean.setDscp(null);
    // 返回值
    return userDao.modifypwd(userBean);
  }

  @Override
  public int delete(String username) throws CustomerException {
    //step1.校验用户是否存在
    int icount = this.checkUsername(username);
    if(icount == 0) {
      throw new CustomerException(1002, "用户已删除");
    }
    //step2.删除用户
    // 返回值
    return userDao.delete(username);
  }
}

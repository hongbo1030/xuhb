package com.grandata.www.grandc.home.user.service;

import com.grandata.www.grandc.common.exception.CustomerException;

public interface IUserService {

  public int checkUsernamePassword(String username, String password);

  public int checkUsername(String username);

  public int create(String username, String password) throws CustomerException;

  public int modifypwd(String username, String password) throws CustomerException;

  public int delete(String username) throws CustomerException;
}

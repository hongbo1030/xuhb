package com.grandata.www.grandc.home.user.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.grandata.www.grandc.domain.user.UserBean;

@Repository("userdao")
public class UserDao {

  @Autowired
  public SqlSessionTemplate sqlSessionTemplate;

  private final String statement = "com.grandata.www.grandc.home.user.dao.mapper.UserMapper.";

  public int checkUsernamePassword(UserBean userBean) {
    return sqlSessionTemplate.selectOne(statement + "checkUsernamePassword", userBean);
  }

  public int checkUsername(String username) {
    return sqlSessionTemplate.selectOne(statement + "checkUsername", username);
  }

  public int create(UserBean userBean) {
    return sqlSessionTemplate.insert(statement + "insertUser", userBean);
  }

  public int modifypwd(UserBean userBean) {
    return sqlSessionTemplate.update(statement + "modifyuserpwd", userBean);
  }

  public int delete(String username) {
    return sqlSessionTemplate.delete(statement + "deleteUser", username);
  }
}

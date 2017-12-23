package com.grandata.www.grandc.domain.user;

import java.io.Serializable;

public class UserBean implements Serializable {

  /**
	 *
	 */
  private static final long serialVersionUID = 1L;

  private int userid;

  private String username;

  private String password;

  private int status;

  private String createtime;

  private String updatetime;

  private String dscp;

  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }

  public String getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(String updatetime) {
    this.updatetime = updatetime;
  }

  public String getDscp() {
    return dscp;
  }

  public void setDscp(String dscp) {
    this.dscp = dscp;
  }

  @Override
  public String toString() {
    return "UserBean [userid=" + userid + ", username=" + username + ", password=" + password
        + ", status=" + status + ", createtime=" + createtime + ", updatetime=" + updatetime
        + ", dscp=" + dscp + "]";
  }

}

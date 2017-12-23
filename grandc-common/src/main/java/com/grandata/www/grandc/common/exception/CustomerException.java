package com.grandata.www.grandc.common.exception;

public class CustomerException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private int errorcode ;  //异常对应的返回码
  private String errormsg;  //异常对应的描述信息

  public CustomerException() {
      super();
  }

  public CustomerException(int errorcode, String errormsg) {
      super();
      this.errorcode = errorcode;
      this.errormsg = errormsg;
  }

  public int getErrorcode() {
    return errorcode;
  }

  public void setErrorcode(int errorcode) {
    this.errorcode = errorcode;
  }

  public String getErrormsg() {
    return errormsg;
  }

  public void setErrormsg(String errormsg) {
    this.errormsg = errormsg;
  }
}

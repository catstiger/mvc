package com.github.catstiger.mvc.resolver;

public class JsonModel {
  /**
   * 没有错误
   */
  public static final String ERROR_NONE = "0";
  
  /**
   * 未知错误
   */
  public static final String ERROR_UNKNOWN = "-1";
  
  private String msg;
  private Boolean successed = true;
  private String errorCode = ERROR_NONE;
  private Object data;
  
  /**
   * 操作成功，构造一个用于渲染的操作成功的Model
   */
  public JsonModel(Object data) {
    this.data = data;
  }
  
  /**
   * 操作失败，传入错误信息
   */
  public JsonModel(String errorMsg) {
    this.msg = errorMsg;
    this.errorCode = ERROR_UNKNOWN;
    this.successed = false;
  }
  
  /**
   * 操作失败，传入错误代码和错误信息
   * @param errorCode 错误代码
   * @param errorMsg 错误信息
   */
  public JsonModel(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.msg = errorMsg;
    this.successed = false;
  }
  
  /**
   * 错误信息
   */
  public String getMsg() {
    return msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  /**
   * 操作是否成功
   * @return
   */
  public Boolean getSuccessed() {
    return successed;
  }
  
  public void setSuccessed(Boolean successed) {
    this.successed = successed;
  }
  
  /**
   * 错误代码
   */
  public String getErrorCode() {
    return errorCode;
  }
  
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
  /**
   * 数据
   */
  public Object getData() {
    return data;
  }
  
  public void setData(Object data) {
    this.data = data;
  }
}

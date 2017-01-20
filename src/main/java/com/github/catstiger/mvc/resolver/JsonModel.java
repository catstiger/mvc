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
  
  private String errorCode = ERROR_NONE;
  private Object data;
  private boolean failed = false;
  
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
    this.failed = true;
  }
  
  /**
   * 操作失败，传入错误代码和错误信息
   * @param errorCode 错误代码
   * @param errorMsg 错误信息
   */
  public JsonModel(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.msg = errorMsg;
    this.failed = (errorCode != ERROR_NONE);
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

  public boolean isFailed() {
    return failed;
  }

  public void setFailed(boolean failed) {
    this.failed = failed;
  }
}

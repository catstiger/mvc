package com.github.catstiger.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RequestObjectHolder {
  private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
  private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>();
  
  private RequestObjectHolder() {
    
  }
  
  static void clear() {
    requestHolder.set(null);
    requestHolder.remove();
    responseHolder.set(null);
    responseHolder.remove();
  }
  
  static void setRequest(HttpServletRequest request) {
    requestHolder.set(request);
  }
  
  static void setResponse(HttpServletResponse response) {
    responseHolder.set(response);
  }
  
  
  
  /**
   * 返回当前请求的HttpServletRequest对象
   */
  public static HttpServletRequest getRequest() {
    return requestHolder.get();
  }
  
  /**
   * @return 当前线程的HttpServletResponse对象
   */
  public static HttpServletResponse getResponse() {
    return responseHolder.get();
  }
}

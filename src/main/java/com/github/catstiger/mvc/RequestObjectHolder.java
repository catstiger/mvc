package com.github.catstiger.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.util.ValueMapUtils;

public final class RequestObjectHolder {
  private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
  private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>();
  private static final ThreadLocal<Map<String, Object>> parametersHolder = new ThreadLocal<Map<String, Object>>();
  private static final ThreadLocal<Map<String, Object>> inheritableParamHolder = new ThreadLocal<Map<String, Object>>();
  
  private RequestObjectHolder() {
    
  }
  
  static void clear() {
    requestHolder.remove();
    responseHolder.remove();
    parametersHolder.remove();
    inheritableParamHolder.remove();
  }
  
  static void setRequest(HttpServletRequest request) {
    if(request == null) {
      clear();
    } else {
      requestHolder.set(request);
    }
  }
  
  static void setResponse(HttpServletResponse response) {
    if(response == null) {
      clear();
    } else {
      responseHolder.set(response);
    }
  }
  
  static void setRequestParameters(Map<String, Object> params) {
    if(params == null || params.isEmpty()) {
      clear();
    } else {
      parametersHolder.set(params);
    }
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
  
  /**
   * 
   * @return 当前线程的的Request的参数
   */
  public static Map<String, Object> getParameters() {
    return parametersHolder.get();
  }
  
  /**
   * 
   * @see {@link ValueMapUtils#inheritableParams(Map, Map)}
   */
  public static Map<String, Object> getInheritableParams() {
    if(inheritableParamHolder.get() == null) {
      Map<String, Object> values = parametersHolder.get();
      if(values == null || values.isEmpty()) {
        return Collections.emptyMap();
      }
      Map<String, Object> inheritable = new HashMap<String, Object>();
      ValueMapUtils.inheritableParams(values, inheritable);
      inheritableParamHolder.set(inheritable);
      return inheritable;
    } else {
      return inheritableParamHolder.get();
    }
  }

}

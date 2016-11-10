package com.github.catstiger.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ClassUtils;

public final class ServletObjectHolder {
  private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
  private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>();
  private static final ThreadLocal<Map<String, Object>> parametersHolder = new ThreadLocal<Map<String, Object>>();
  private static final ThreadLocal<Map<String, Object>> inheritableParamHolder = new ThreadLocal<Map<String, Object>>();
  
  private ServletObjectHolder() {
    
  }
  
  static void clear() {
    requestHolder.remove();
    responseHolder.remove();
    parametersHolder.remove();
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
  
  public static void setRequestParameters(Map<String, Object> params) {
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
   * 整理Request参数，将一个长度的数组转换为单个String，将带有"."的参数名称，按照一个单独的属性对待。
   * @return
   */
  public static Map<String, Object> getInheritableParams() {
    if(inheritableParamHolder.get() == null) {
      Map<String, Object> values = parametersHolder.get();
      if(values == null || values.isEmpty()) {
        return Collections.emptyMap();
      }
      Map<String, Object> inheritable = new HashMap<String, Object>();
      inheritableParams(values, inheritable);
      inheritableParamHolder.set(inheritable);
      return inheritable;
    } else {
      return inheritableParamHolder.get();
    }
  }
  
  /**
   * 将一个Request参数Map，根据其Key的特征，转换为一个带有层级结构的Map对象，具体：
   * <p>
   * HttpServletRequest的参数Map通常是这样的：<br>
   * {<br>
   *    "name3" : "Sam",<br>
   *    "name2":  "Lee",<br>
   *    "name1.field1" : "Tech",<br>
   *    "name1.field2" : "f2"<br>
   *    "name1.parent.name" : "Li"<br>
   * }
   * <br>
   * 转换后：<br>
   * {<br>
   *    "name3" : "Sam",<br>
   *    "name2":  "Lee",<br>
   *    "name1": {
   *       "field1" : "Tech",<br>
   *       "field2" : "f2",<br>
   *       "parent": {<br>
   *            "name" : "Li"<br>
   *        }<br>
   *    }
   *    <br>
   * }
   * </p>
   * @param params
   * @param inheritable
   */
  @SuppressWarnings("unchecked")
  private static void inheritableParams(Map<String, Object> params, Map<String, Object> inheritable) {
    if(inheritable == null) {
      return;
    }
    if(params == null || params.isEmpty()) {
      return;
    }
    
    Set<String> keys = params.keySet();
    for(Iterator<String> itr = keys.iterator(); itr.hasNext();) {
      String key = itr.next();
      int dotIndex = key.indexOf(".");
      if(dotIndex > 0) {
        String prefix = key.substring(0, dotIndex);
        Map<String, Object> subParams = null;
        Object item = inheritable.get(prefix);
        if(item != null && ClassUtils.isAssignable(Map.class, item.getClass())) {
          subParams = (Map<String, Object>) item;
        } else {
          subParams = new HashMap<String, Object>(10);
          inheritable.put(prefix, subParams);
        }
        subParams.put(key.substring(dotIndex + 1), params.get(key));
      } else {
        Object vo = params.get(key);
        if(vo == null) {
          inheritable.put(key, null);
        } else if (!vo.getClass().isArray()) {
          inheritable.put(key, vo);
        } else {
          String[] value = (String[]) vo;
          if(value.length == 0) {
            inheritable.put(key, null);
          } else if (value.length == 1) {
            inheritable.put(key, value[0]);
          } else {
            inheritable.put(key, value);
          }
        }
       
      }
    }
    
    keys = inheritable.keySet();
    for(Iterator<String> itr = keys.iterator(); itr.hasNext();) {
      String key = itr.next();
      Object val = inheritable.get(key);
      if(ClassUtils.isAssignable(Map.class, val.getClass())) {
        Map<String, Object> map = new HashMap<String, Object>();
        inheritableParams((Map<String, Object>) val, map);
        inheritable.put(key, map);
      }
    }
    
  }
  
}

package com.github.catstiger.mvc.resolver;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.github.catstiger.mvc.annotation.API;
import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.util.ReflectUtils;

public final class ResolverFactory {
  private static Map<String, ResponseResolver> successResolverCache = new ConcurrentHashMap<String, ResponseResolver>(160);
  private static final ResponseResolver DEFAULT_JSON_505 = new DefaultJson505Resolver();
  private static final ResponseResolver DEFAULT_JSP_505 = new DefaultJsp505Resolver();
  private static final ResponseResolver DEFAULT_JSON = new DefaultJsonSuccessResolver();
  private static final ResponseResolver DEFAULT_JSP = new DefaultJspSuccessResolver();
  
  
  /**
   * 根据URI对应的Method的标注（API），或者根据Request，创建ResponseResolver的实例
   * @param request HttpServletRequest
   * @return 
   */
  public static ResponseResolver getSuccessResolver(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      uri = "";
    }
    if(successResolverCache.containsKey(uri)) {
      return successResolverCache.get(uri);
    }
    //处理自定义Resolver
    String serviceId = RequestParser.getRequestUri(request);
    ApiResource apiResource = ApiResHolder.getInstance().getApiResource(serviceId);
    
    
    Method method = apiResource.getMethod();
    if(method != null) {
      API api = method.getAnnotation(API.class);
      if(api != null) {
        if(api.resolver() != ResponseResolver.None.class) {
          ResponseResolver resolver = ReflectUtils.instantiate(api.resolver());
          successResolverCache.put(uri, resolver);
          return resolver;
        }
      }
    }
    //处理缺省Resolver
    if(RequestParser.isJsonRequest(request)) {
      return DEFAULT_JSON;
    } else {
      return DEFAULT_JSP;
    }
  }
  
  /**
   * 创建处理505错误的ResponseResolver实例
   * @param request HttpServletRequest
   */
  public static ResponseResolver getFailureResolver(HttpServletRequest request) {
    String uri = RequestParser.getRequestUri(request);
    if(uri == null) {
      uri = "";
    }
    
    if(RequestParser.isJsonRequest(request)) {
      return DEFAULT_JSON_505;
    } else {
      return DEFAULT_JSP_505;
    }
  }
  
  
  
  private ResolverFactory() {
    
  }
  
}

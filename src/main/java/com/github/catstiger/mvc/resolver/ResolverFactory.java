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
    ApiResource apiResource = ApiResHolder.getInstance().getApiResource(request.getRequestURI());
    
    if(successResolverCache.containsKey(uri)) {
      return successResolverCache.get(uri);
    }
    
    ResponseResolver resolver;
    Method method = apiResource.getMethod();
    if(method != null) {
      API api = method.getAnnotation(API.class);
      if(api != null) {
        if(api.resolver() != ResponseResolver.None.class) {
          resolver = ReflectUtils.instantiate(api.resolver());
          successResolverCache.put(uri, resolver);
          return resolver;
        }
      }
    }
    
    if(RequestParser.isJsonRequest(request)) {
      resolver = new DefaultJsonSuccessResolver();  
    } else {
      resolver = new DefaultJspSuccessResolver();
    }
    successResolverCache.put(uri, resolver);
    return resolver;
  }
  
  /**
   * 创建处理505错误的ResponseResolver实例
   * @param request HttpServletRequest
   */
  public static ResponseResolver getFailureResolver(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if(uri == null) {
      uri = "";
    }
    if(successResolverCache.containsKey(uri)) {
      return successResolverCache.get(uri);
    }
    
    ResponseResolver resolver;
    if(RequestParser.isJsonRequest(request)) {
      resolver = new DefaultJson505Resolver();  
    } else {
      resolver = new DefaultJsp505Resolver();
    }
    successResolverCache.put(uri, resolver);
    return resolver;
  }
  
  
  
  private ResolverFactory() {
    
  }
  
}

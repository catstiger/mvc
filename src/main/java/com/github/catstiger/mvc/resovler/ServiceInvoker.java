package com.github.catstiger.mvc.resovler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.service.ServiceProvider;
import com.github.catstiger.mvc.service.ServiceProviderFactory;

import strman.Strman;

public abstract class ServiceInvoker {
  /**
   * 
   * @param apiResource
   * @param cascadedParams
   * @return
   */
  public static Object invoke(ApiResource apiResource, Map<String, Object> cascadedParams) {
    if(apiResource == null) {
      return null;
    }
    
    ServiceProvider serviceProvider = ServiceProviderFactory.getServiceProvider(apiResource);
    if(serviceProvider == null) {
      throw new RuntimeException("No service provider found. " + apiResource.getUri());
    }
    
    Object svr = serviceProvider.getService(apiResource);
    if(svr == null) {
      throw new RuntimeException("No service found. " + apiResource.getServiceId());
    }
    
    Method method = apiResource.getMethod();
    Method realMethod = method;
    try {
      realMethod = svr.getClass().getMethod(apiResource.getServiceId(), method.getParameterTypes());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    if(realMethod == null) {
      realMethod = method;
    }
    
    Object[] args = new Object[method.getParameterTypes().length];
    Parameter[] params = realMethod.getParameters();
    for(Parameter param : params) {
      String paramName = getParameterName(param);
    }
        
    return null;
  }
  
  private static String getParameterName(Parameter parameter) {
    if(parameter == null) {
      return null;
    }
    if(parameter.isNamePresent()) {
      return parameter.getName();
    }
    Param param = parameter.getAnnotation(Param.class);
    if(param != null && StringUtils.isNotBlank(param.value())) {
      return param.value();
    }
    
    return Strman.toCamelCase(parameter.getType().getSimpleName());
  }
}

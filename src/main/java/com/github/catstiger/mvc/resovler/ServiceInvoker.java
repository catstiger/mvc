package com.github.catstiger.mvc.resovler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.converter.ValueConverter;
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
    
    Method method = null;
    try {
      method = svr.getClass().getMethod(apiResource.getMethodName(), apiResource.getMethod().getParameterTypes());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    Parameter[] params = method.getParameters();
    Object[] args = new Object[params.length];
    if(params.length == 1) {
      Class<?> paramType = params[0].getType();
      if(paramType.isArray() || paramType == List.class || paramType == Set.class) {
        args[0] = doSingleArray(params[0], cascadedParams);
      } else {
        args[0] = doSingleBean(params[0], cascadedParams);
      }
    } else {
      for(int i = 0; i < params.length; i++) {
        Object value = getParamValue(params[i], cascadedParams, i);
        args[i] = value;
      }
    }
        
    try {
      return method.invoke(svr, args);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  
  private static Object doSingleArray(Parameter parameter, Map<String, Object> cascadedParams) {
    if(cascadedParams == null || cascadedParams.isEmpty()) {
      return null;
    }
    Class<?> paramType = parameter.getType();
    ValueConverter<?> converter = null;
    if(paramType == List.class || paramType == Set.class) {
      Class<?> elementType = null;
      
      Param param = parameter.getAnnotation(Param.class);
      if(param != null && param.elementType() != null && param.elementType() != Object.class) {
        elementType = param.elementType();
        converter = ConverterFactory.getConverter(paramType, elementType);
      }
    } else if (paramType.isArray()) {
      converter = ConverterFactory.getConverter(paramType, paramType.getComponentType());
    }
    if(converter == null) {
      throw new RuntimeException("无法找到转换器");
    }
    
    return converter.convert(cascadedParams);
  }
  
  private static Object doSingleBean(Parameter parameter, Map<String, Object> cascadedParams) {
    if(cascadedParams == null || cascadedParams.isEmpty()) {
      return null;
    }
    Class<?> paramType = parameter.getType();
    ValueConverter<?> converter = ConverterFactory.getConverter(paramType);
    
    return converter.convert(cascadedParams);
  }
  
  private static Object getParamValue(Parameter parameter, Map<String, Object> cascadedParams, int paramIndex) {
    if(CollectionUtils.isEmpty(cascadedParams)) {
      return null;
    }
    String paramName = getParameterName(parameter, paramIndex);
    Object value = cascadedParams.get(paramName);
    if(value == null) {
      return null;
    }
    Class<?> paramType = parameter.getType();
    Class<?> elementType = null;
    
    Param param = parameter.getAnnotation(Param.class);
    if(param != null && param.elementType() != null && param.elementType() != Object.class) {
      elementType = param.elementType();
    }
    
    ValueConverter<?> converter = ConverterFactory.getConverter(paramType, elementType);
    return converter.convert(value);
  }
  
  private static String getParameterName(Parameter parameter, int paramIndex) {
    if(parameter == null) {
      return null;
    }
    //对于Present的名字，直接返回参数名称
    if(parameter.isNamePresent()) {
      return parameter.getName();
    }
    //对于用Param标注的参数，返回Param规定的名称
    Param param = parameter.getAnnotation(Param.class);
    if(param != null && StringUtils.isNotBlank(param.value())) {
      return param.value();
    }
    //对于进没有Present也没有@Param的参数，返回参数类名（驼峰命名）+参数位置索引
    return Strman.toCamelCase(parameter.getType().getSimpleName()) + paramIndex;
  }
}

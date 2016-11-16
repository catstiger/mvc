package com.github.catstiger.mvc.resovler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.annotation.Param;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.converter.ValueConverter;
import com.github.catstiger.mvc.service.ServiceProvider;
import com.github.catstiger.mvc.service.ServiceProviderFactory;
import com.github.catstiger.mvc.util.CollectionUtils;
import com.github.catstiger.mvc.util.ReflectUtils;
import com.github.catstiger.mvc.util.StringUtils;

import strman.Strman;

public abstract class ServiceInvoker {
  private static Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);
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
    if(method == null) {
      throw new RuntimeException("没有处理此URI的方法, " +apiResource.getUri());
    }
    
    Parameter[] params = method.getParameters();
    Object[] args = new Object[params.length];
    
    if(cascadedParams == null) {
      cascadedParams = Collections.emptyMap();
    }
    //当只有一个参数，且参数为POJO，允许参数属性直接作为KEY
    if(params.length == 1 && ConverterFactory.isPojo(params[0].getType()) && !cascadedParams.containsKey(params[0].getName())) {
      Class<?> paramType = params[0].getType();
      ValueConverter<?> converter = ConverterFactory.getConverter(paramType);
      args[0] = converter.convert(cascadedParams);
    } 
    else { //多个参数，或者单个primitive\collection
      for(int i = 0; i < params.length; i++) {
        Object value = getParamValue(params[i], cascadedParams, i);
        args[i] = value;
      }
    }
    
    return ReflectUtils.invokeMethod(method, svr, args);
  }
  
  private static Object getParamValue(Parameter parameter, Map<String, Object> cascadedParams, int paramIndex) {
    if(CollectionUtils.isEmpty(cascadedParams)) {
      cascadedParams = Collections.emptyMap();
    }
    String paramName = getParameterName(parameter, paramIndex);
    Class<?> paramType = parameter.getType();
    Class<?> elementType = null;
    
    Param param = parameter.getAnnotation(Param.class);
    if(param != null && param.elementType() != null && param.elementType() != Object.class) {
      elementType = param.elementType();
    }
    
    ValueConverter<?> converter = ConverterFactory.getConverter(paramType, elementType);
    logger.debug("转换器 {} {}", paramType.getName(), converter.getClass().getName());
    
    Object value = cascadedParams.get(paramName);
    
    return converter.convert(value);
  }
  
  public static String getParameterName(Parameter parameter, int paramIndex) {
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

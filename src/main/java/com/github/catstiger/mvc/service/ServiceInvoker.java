package com.github.catstiger.mvc.service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;

import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.converter.ValueConverter;
import com.github.catstiger.mvc.util.CollectionUtils;
import com.github.catstiger.mvc.util.ReflectUtils;

public abstract class ServiceInvoker {
  /**
   * 
   * @param apiResource
   * @param cascadedParams
   * @return
   */
  public static Object invoke(ApiResource apiResource, Map<String, Object> cascadedParams) {
    if (apiResource == null) {
      return null;
    }

    ServiceProvider serviceProvider = ServiceProviderFactory.getServiceProvider(apiResource);
    if (serviceProvider == null) {
      throw new RuntimeException("No service provider found. " + apiResource.getUri());
    }

    Object svr = serviceProvider.getService(apiResource);
    if (svr == null) {
      throw new RuntimeException("No service found. " + apiResource.getServiceId());
    }

    Method method = apiResource.getMethod();
    if (method == null) {
      throw new RuntimeException("没有处理此URI的方法, " + apiResource.getUri());
    }

    Parameter[] params = method.getParameters();
    Object[] args = new Object[params.length];

    if (cascadedParams == null) {
      cascadedParams = Collections.emptyMap();
    }
    // 当只有一个参数，且参数为POJO，并且允许参数属性直接作为KEY
    if (params.length == 1 && ConverterFactory.isPojo(params[0].getType()) && !cascadedParams.containsKey(ReflectUtils.getParameterName(params[0], 0))) {
      ValueConverter<?> converter = ConverterFactory.getConverter(params[0]);
      args[0] = converter.convert(cascadedParams);
    } else { // 多个参数，或者单个primitive\collection
      for (int i = 0; i < params.length; i++) {
        Object value = getParamValue(params[i], cascadedParams, i);
        args[i] = value;
      }
    }

    return ReflectUtils.invokeMethod(method, svr, args);
  }

  private static Object getParamValue(Parameter parameter, Map<String, Object> cascadedParams, int paramIndex) {
    if (CollectionUtils.isEmpty(cascadedParams)) {
      cascadedParams = Collections.emptyMap();
    }
    String paramName = ReflectUtils.getParameterName(parameter, paramIndex);
    ValueConverter<?> converter = ConverterFactory.getConverter(parameter);
    Object value = cascadedParams.get(paramName);

    return converter.convert(value);
  }
}

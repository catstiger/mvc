package com.github.catstiger.mvc.service;

import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.util.ReflectUtils;

public class ServiceProviderCreate implements ServiceProvider {

  @Override
  public Object getService(ApiResource apiResource) {
    Object bean;
    
    if(apiResource.getSingleton()) { //单例
      if(apiResource.getServiceInstance() == null) { //未初始化
        apiResource.setServiceInstance(createInstance(apiResource.getServiceId()));
      }
      bean = apiResource.getServiceInstance();
    } else {
      bean = createInstance(apiResource.getServiceId());
    }
    return bean;
  }
  
  private Object createInstance(String className) {
    Class<?> clazz;
    try {
      clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
      return ReflectUtils.instantiate(clazz);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}

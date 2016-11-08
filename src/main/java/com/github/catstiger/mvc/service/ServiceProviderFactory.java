package com.github.catstiger.mvc.service;

import java.util.HashMap;
import java.util.Map;

import com.github.catstiger.mvc.config.ApiResource;

public final class ServiceProviderFactory {
  private static Map<String, ServiceProvider> svrProviders = new HashMap<String, ServiceProvider>();
  static {
    svrProviders.put(ServiceProvider.SERVICE_PROVIDER_CREATE, new ServiceProviderCreate());
    svrProviders.put(ServiceProvider.SERVICE_PROVIDER_SPRING, new ServiceProviderSpring());
  }
  
  public static ServiceProvider getServiceProvider(ApiResource apiResource) {
    if(apiResource == null) {
      throw new RuntimeException("ApiResource is not existed.");
    }
    if(!svrProviders.containsKey(apiResource.getProviderType())) {
      throw new RuntimeException("无法判断处理此资源的服务提供者.");
    }
    
    return svrProviders.get(apiResource.getProviderType());
  }
  
  private ServiceProviderFactory() {
    
  }
}

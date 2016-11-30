package com.github.catstiger.mvc.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.util.StringUtils;

public final class ApiResHolder {
  private static Logger logger = LoggerFactory.getLogger(ApiResHolder.class);
  
  private static ConcurrentMap<String, ApiResource> apiMapping = new ConcurrentHashMap<String, ApiResource>(160);
  private static ApiResHolder instance = null;
  
  public void add(ApiResource apiResource) {
    if(apiResource == null) {
      return;
    }  
    
    if(StringUtils.isBlank(apiResource.getUri())) {
      logger.warn("URI is null, ignored");
      return;
    }
    if(apiMapping.containsKey(apiResource.getUri())) {
      logger.warn("URI is duplicated [{}]", apiResource.getUri());
    }
    
    logger.debug("Add URI [{}]", apiResource.getUri());
    apiMapping.put(apiResource.getUri(), apiResource);
  }
  
  public ApiResource getApiResource(String serviceId) {
    if(StringUtils.isBlank(serviceId)) {
      logger.debug("Required uri is blank.");
      return null;
    }
    if(serviceId.endsWith("/")) {
      serviceId = StringUtils.removeRight(serviceId, "/");
    }
    
    if(apiMapping.containsKey(serviceId)) { //直接根据URI返回对应的ApiResource对象
      return apiMapping.get(serviceId);
    }
    
    return null;
  }
  
  public static ApiResHolder getInstance() {
    if(instance == null) {
      instance = new ApiResHolder();
    }
    
    return instance;
  }
  
  private ApiResHolder() {
    
  }
}

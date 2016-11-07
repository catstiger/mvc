package com.github.catstiger.mvc.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strman.Strman;

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
    
    
    apiMapping.put(apiResource.getUri(), apiResource);
  }
  
  /**
   * 根据URI，获取处理此URI的ApiResource对象，URI可以与原始URI不同，支持下划线，横杠作为字符分隔符，例如:<br>
   * /cats_tiger/do_something可以匹配/catsTiger/doSomething
   * @param uri
   * @return
   */
  public ApiResource getApiResource(String uri) {
    if(StringUtils.isBlank(uri)) {
      logger.debug("Required uri is blank.");
      return null;
    }
    if(uri.endsWith("/")) {
      uri = Strman.removeRight(uri, "/");
    }
    
    if(apiMapping.containsKey(uri)) { //直接根据URI返回对应的ApiResource对象
      return apiMapping.get(uri);
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

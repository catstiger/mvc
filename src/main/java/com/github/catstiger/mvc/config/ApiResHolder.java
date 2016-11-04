package com.github.catstiger.mvc.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import strman.Strman;

public class ApiResHolder {
  private static Logger logger = LoggerFactory.getLogger(ApiResHolder.class);
  
  private static ConcurrentMap<String, ApiResource> apiMapping = new ConcurrentHashMap<String, ApiResource>(160);
  
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
    
    if(apiMapping.containsKey(uri)) { //直接根据URI返回对应的ApiResource对象
      return apiMapping.get(uri);
    } else {
      String[] segments = StringUtils.split(uri, "/");
      if(ArrayUtils.isEmpty(segments)) {
        logger.debug("Required uri is blank?");
        return null;
      }
      //如果URI只有一个号段，则缺省的认为service为处理该URI的方法名
      if(segments.length == 1) {
        segments = ArrayUtils.add(segments, "service");
      }
      //支持下划线，全大写，横杠等命名方式
      String[] keySegments = new String[2];
      
      for(int i = 0; i < 2; i++) {
        keySegments[i] = Strman.toCamelCase(segments[i]);
      }
      
      String key = Joiner.on("/").join(keySegments);
      
      if(apiMapping.containsKey(key)) {
        apiMapping.put(uri, apiMapping.get(key));
      }
      return apiMapping.get(key);
    }
  }
  
  public static void main(String[] args) {
    System.out.println(Strman.toCamelCase("CatsTiger"));
    System.out.println(Strman.toCamelCase("cats_tiger"));
    System.out.println(Strman.toCamelCase("Cats-Tiger"));
  }
}

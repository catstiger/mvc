package com.github.catstiger.mvc.resovler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.ServletObjectHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.service.ServiceProvider;

import junit.framework.TestCase;

public class ServerInvokerTest extends TestCase {
  @Test
  public void testPrimitive() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("age", new String[]{"20"});
    map.put("id", new String[]{"859684"});
    map.put("birth", new String[] {"1996-09-08"});
    map.put("age", new String[]{"20"});
    map.put("score", new String[] {"90.98"});
    map.put("isActive", new String[] {"true"});
    
    ServletObjectHolder.setRequestParameters(map);
    Map<String, Object> params = ServletObjectHolder.getInheritableParams();
    System.out.println(JSON.toJSONString(params));
    ApiResource apiRes = new ApiResource();
    
    apiRes.setServiceId(TestService.class.getName());
    apiRes.setProviderType(ServiceProvider.SERVICE_PROVIDER_CREATE);
    apiRes.setMethodName("testPrimitive");
    Method[] methods = TestService.class.getMethods();
    for(int i = 0; i < methods.length; i++) {
      if(methods[i].getName().equals("testPrimitive")) {
        apiRes.setMethod(methods[i]);
        break;
      }
    }
    apiRes.setSingleton(true);
    apiRes.setUri("/test_service/test_primitive");
    
    String json = (String) ServiceInvoker.invoke(apiRes, params);
    System.out.println(json);
  }
  
  @Test
  public void testSingleBean() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("age", new String[]{"120"});
    map.put("id", new String[]{"887777"});
    map.put("birth", new String[] {"1996-11-08"});
    map.put("age", new String[]{"21"});
    map.put("score", new String[] {"900.98D"});
    map.put("isActive", new String[] {"null"});
    
    ServletObjectHolder.setRequestParameters(map);
    Map<String, Object> params = ServletObjectHolder.getInheritableParams();
    System.out.println(JSON.toJSONString(params));
    ApiResource apiRes = new ApiResource();
    
    apiRes.setServiceId(TestService.class.getName());
    apiRes.setProviderType(ServiceProvider.SERVICE_PROVIDER_CREATE);
    apiRes.setMethodName("testSingleBean");
    Method[] methods = TestService.class.getMethods();
    for(int i = 0; i < methods.length; i++) {
      if(methods[i].getName().equals("testSingleBean")) {
        apiRes.setMethod(methods[i]);
        break;
      }
    }
    apiRes.setSingleton(true);
    apiRes.setUri("/test_service/test_single_bean");
    
    String json = (String) ServiceInvoker.invoke(apiRes, params);
    System.out.println(json);
  }
}

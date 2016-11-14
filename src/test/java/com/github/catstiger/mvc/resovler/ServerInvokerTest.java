package com.github.catstiger.mvc.resovler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.AbstractTestCase;
import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.service.ServiceProvider;
import com.github.catstiger.mvc.util.ValueMapUtils;

public class ServerInvokerTest extends AbstractTestCase {
  @Test
  public void testPrimitive() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("age", new String[]{"20"});
    map.put("id", new String[]{"859684"});
    map.put("birth", new String[] {"1996-09-08"});
    map.put("age", new String[]{"20"});
    map.put("score", new String[] {"90.98"});
    map.put("isActive", new String[] {"true"});
    
    Map<String, Object> params = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(map, params);
   
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
    assertNotNull(json);
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
    
    Map<String, Object> testParam = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(map, testParam);
    
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
    
    String json = (String) ServiceInvoker.invoke(apiRes, testParam);
    assertNotNull(json);
  }
  
  @Test
  public void testAny() {
    Initializer.getInstance().loadApiResources("com.github.catstiger");
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(testData, testParam);
    
    System.out.println("****" + JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testSingleValue() {
    Initializer.getInstance().loadApiResources("com.github.catstiger");
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_value");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_value");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    System.out.println(JSON.toJSONString(testData));
    
    Map<String, Object> testParam = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(testData, testParam);
    System.out.println(JSON.toJSONString(testParam));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testSinglePrimitiveArray() {
    Initializer.getInstance().loadApiResources("com.github.catstiger");
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_primitive_array");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_primitive_array");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = new HashMap<String, Object>();
    ValueMapUtils.inheritableParams(testData, testParam);
    System.out.println(JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
    
  }
  
}

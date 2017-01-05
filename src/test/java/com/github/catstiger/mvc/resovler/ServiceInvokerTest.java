package com.github.catstiger.mvc.resovler;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.github.catstiger.mvc.AbstractTestCase;
import com.github.catstiger.mvc.ServletObjectHolderMock;
import com.github.catstiger.mvc.SpringHelper;
import com.github.catstiger.mvc.config.ApiResHolder;
import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.service.ServiceInvoker;
import com.github.catstiger.mvc.service.ServiceProvider;
import com.github.catstiger.utils.ValueMapUtils;

public class ServiceInvokerTest extends AbstractTestCase {
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    System.out.println("Init application context...");
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-test.xml");
    SpringHelper.initApplicationContext(ctx);  
    ServletObjectHolderMock.init(); //Mock
    Initializer.getInstance().loadApiResources("com.github.catstiger");
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }
  @Test
  public void testPrimitive() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("age", new String[]{"20"});
    map.put("id", new String[]{"859684"});
    map.put("birth", new String[] {"1996-09-08"});
    map.put("age", new String[]{"20"});
    map.put("score", new String[] {"90.98"});
    map.put("isActive", new String[] {"true"});
    
    Map<String, Object> params = ValueMapUtils.inheritableParams(map);
    
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
    
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(map);
    
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
    
    //使用自动生成的数据
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_bean");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_bean");
    }
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    testParam = ValueMapUtils.inheritableParams(testData);
    json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testAny() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    System.out.println("****" + JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testAnyWithNull() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> test = new HashMap<String, Object>();
    for(Iterator<String> itr = testData.keySet().iterator(); itr.hasNext();) {
      String key = itr.next();
      if(!key.startsWith("dept")) {
        test.put(key, testData.get(key));
      }
    }
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(test);
    ServiceInvoker.invoke(api, testParam);
    
    api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_bean");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_bean");
    }
    testData = this.prepareTestData(api.getMethod(), true);
    test = new HashMap<String, Object>();
    for(Iterator<String> itr = testData.keySet().iterator(); itr.hasNext();) {
      String key = itr.next();
      if(!key.startsWith("score") && !key.startsWith("birth")) {
        test.put(key, testData.get(key));
      }
    }
    testParam = ValueMapUtils.inheritableParams(test);
    ServiceInvoker.invoke(api, testParam);
    
    ServiceInvoker.invoke(api, null);
  }
  
 
  @Test
  public void testSingleValue() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_value");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_value");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    System.out.println(JSON.toJSONString(testData));
    
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    System.out.println(JSON.toJSONString(testParam));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testSinglePrimitiveArray() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_primitive_array");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_primitive_array");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    System.out.println(JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
    
  }
  
  @Test
  public void testSingleDateArray() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_date_array");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_single_date_array");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    System.out.println(JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
    
  }
  
  @Test
  public void testEmptyArgs() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_empty_args");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_empty_args");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    System.out.println(JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testEmptyArgsManyTimes() {
    
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_empty_args");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_empty_args");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    long b = new Date().getTime();
    for(int i = 0; i < 100 * 10000; i++) {
      ServiceInvoker.invoke(api, testParam);
    }
    System.out.println((new Date().getTime() - b) / 1000);
  }
  
  @Test
  public void testHttp() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_http");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_http");
    }
    ServiceInvoker.invoke(api, null);
    
    api = ApiResHolder.getInstance().getApiResource("/test_service/test_request");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_request");
    }
    ServiceInvoker.invoke(api, null);
  }
  
  @Test
  public void testHttpAndOther() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_http_and_other");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_http_and_other");
    }
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    ServiceInvoker.invoke(api, testParam);
  }
  
  @Test
  public void testCollection() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_list");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_list");
    }
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    System.out.println(JSON.toJSONString(testParam, true));
    ServiceInvoker.invoke(api, testParam);
    
    api = ApiResHolder.getInstance().getApiResource("/test_service/test_set");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_set");
    }
    testData = this.prepareTestData(api.getMethod(), true);
    testParam = ValueMapUtils.inheritableParams(testData);
    
    System.out.println(JSON.toJSONString(testParam, true));
    ServiceInvoker.invoke(api, testParam);
    
    api = ApiResHolder.getInstance().getApiResource("/test_service/test_list_no_param");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_list_no_param");
    }
    testData = this.prepareTestData(api.getMethod(), true);
    testParam = ValueMapUtils.inheritableParams(testData);
    
    System.out.println(JSON.toJSONString(testParam, true));
    ServiceInvoker.invoke(api, testParam);
  }
  
  @Test
  public void testAnySpring() {
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    System.out.println("****" + JSON.toJSONString(testParam, true));
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testAnySpringManyTimes() {
    
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    long b = new Date().getTime();
    for(int i = 0; i < 100 * 10000; i++) {
      ServiceInvoker.invoke(api, testParam);
    }
    long a = new Date().getTime();
    System.out.println((a - b) / 1000 + "s");
  }
  
  @Test
  public void testAnyManyTimes() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_any");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_any");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    long b = new Date().getTime();
    for(int i = 0; i < 100 * 10000; i++) {
      ServiceInvoker.invoke(api, testParam);
    }
    long a = new Date().getTime();
    System.out.println((a - b) / 1000 + "s");
  }
  
  
  
  @Test
  public void testSingleBeanSpringManyTimes() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_single_bean");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_single_bean");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    long b = new Date().getTime();
    for(int i = 0; i < 100 * 10000; i++) {
      ServiceInvoker.invoke(api, testParam);
    }
    long a = new Date().getTime();
    System.out.println((a - b) / 1000 + "s");
  }
  
  @Test
  public void testSingleBeanManyTimes() {
      Map<String, Object> map = new HashMap<String, Object>();
      
      long b = new Date().getTime();
      Map<String, Object> testParam = ValueMapUtils.inheritableParams(map);
      ApiResource api = ApiResHolder.getInstance().getApiResource("/test_service/test_single_bean");
      for(int i = 0; i < 100 * 10000; i++) {
        ServiceInvoker.invoke(api, testParam);
      }
      System.out.println((new Date().getTime() - b) / 1000 + "s");
  }
  
  
  
  @Test
  public void testPrimitiveSpring() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_primitive");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_primitive");
    }
    
    Map<String, Object> testData = this.prepareTestData(api.getMethod(), true);
    Map<String, Object> testParam = ValueMapUtils.inheritableParams(testData);
    
    String json = (String) ServiceInvoker.invoke(api, testParam);
    System.out.println(json);
  }
  
  @Test
  public void testCustomerConverter() {
    ApiResource api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_customer_converter");
    if(api == null) {
      throw new RuntimeException("404, /spring_test_service/test_customer_converter");
    }
    Map<String, Object> testParam = new HashMap<String, Object>();
    testParam.put("param", "ddd");
    ServiceInvoker.invoke(api, testParam);
    
    api = ApiResHolder.getInstance().getApiResource("/spring_test_service/test_customer_converter");
    if(api == null) {
      throw new RuntimeException("404, /test_service/test_customer_converter");
    }
    testParam = new HashMap<String, Object>();
    testParam.put("param", "ddd");
    ServiceInvoker.invoke(api, testParam);
    
    
  }
}
